#!/usr/bin/env bash
#
# verify.sh — run the same verification tasks CI runs, summarize pass/fail, and collate every
# task's artifacts (reports, Paparazzi diffs, the debug APK) into one folder.
#
# Mirrors the verification jobs in .circleci/config.yml. CI splits these across parallel jobs;
# locally they run sequentially and independently: a failing task does NOT stop the others, so one
# run gives you the full picture. Differences from CI, on purpose:
#   - The Gradle daemon is left on (faster local reruns; CI uses --no-daemon in throwaway containers).
#   - --max-workers is left at the machine default.
#   - configuration-cache stays OFF (CI runs --no-configuration-cache; some tasks aren't CC-safe).
#   - The `apk` task builds assembleDebug, not assembleRelease: the release build needs the signing
#     keystore + the google-services.json secret CI injects.
#   - CI's android-lint job (`:apps:android:lintRelease`) is omitted: lintRelease compiles the release
#     variant, which references firebase and can't build locally without the google-services.json
#     secret. CI still runs it; run it locally by hand once you have google-services.json in place.
#   - CI-only jobs are omitted: setup (dependency download), build_release_apk / publish_app_bundle
#     (need secrets + signing). `shared-build` (:apps:jvm:classes) is added — it's not in CI yet, but
#     the desktop app is a first-class target now and this catches JVM-target breakage cheaply.
#
# NOTE: .circleci/config.yml still references pre-restructure module paths (:app,
# :vgls:android:ui:previews). This script uses the current paths (:apps:android,
# :vgls:android:ui:previews:real); the CI config should be updated to match.
#
# Usage:
#   ./verify.sh                       # run everything
#   ./verify.sh detekt screenshot     # run only the named task(s)
#   ./verify.sh --skip-apps           # everything except the heavy android builds (lint + APK)
#   ./verify.sh --rerun               # force every task to re-run (Gradle --rerun-tasks)
#   ./verify.sh --list                # list task names
#   VERIFY_OUT=/tmp/v ./verify.sh     # override the output folder
#
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

OUT="${VERIFY_OUT:-build/verification}"
LOGS="$OUT/logs"
# Match CI's caching posture (build-cache on, configuration-cache off); plain console for grep-able logs.
GRADLE_FLAGS=(--build-cache --console=plain)

# name | kind | command.  kind=gradle -> run "./gradlew <cmd> <flags>"; kind=shell -> run <cmd> as-is.
# Order = cheapest feedback first, then the heavy android builds. Mirrors the CI jobs.
ALL_TASKS=(
  "ktlint|gradle|ktlintCheck --continue"
  "detekt|gradle|detekt --continue"
  "screenshot|gradle|:vgls:android:ui:previews:real:verifyPaparazziDebug --continue"
  "shared-build|gradle|:apps:jvm:classes"
  "apk|gradle|:apps:android:assembleDebug"
)
# The heavy android builds, skippable with --skip-apps.
APP_BUILD_TASKS="apk"

# ---- arg parsing -----------------------------------------------------------
filter=()
skip_apps=0
rerun=0
for arg in "$@"; do
  case "$arg" in
    -h|--help) awk 'NR>1 && /^#/ {sub(/^# ?/, ""); print; next} NR>1 {exit}' "${BASH_SOURCE[0]}"; exit 0 ;;
    --list) printf '%s\n' "${ALL_TASKS[@]%%|*}"; exit 0 ;;
    --skip-apps) skip_apps=1 ;;
    --rerun|--rerun-tasks) rerun=1 ;;
    -*) echo "unknown option: $arg" >&2; exit 2 ;;
    *) filter+=("$arg") ;;
  esac
done

# --rerun forces every task to re-execute via Gradle's --rerun-tasks (ignores up-to-date AND
# build-cache hits), so the run reflects a from-scratch build rather than cached/incremental results.
[ "$rerun" = 1 ] && GRADLE_FLAGS+=(--rerun-tasks)

selected=()
for entry in "${ALL_TASKS[@]}"; do
  name="${entry%%|*}"
  if [ "$skip_apps" = 1 ] && [[ " $APP_BUILD_TASKS " == *" $name "* ]]; then continue; fi
  if [ "${#filter[@]}" -gt 0 ] && [[ " ${filter[*]} " != *" $name "* ]]; then continue; fi
  selected+=("$entry")
done
if [ "${#selected[@]}" -eq 0 ]; then echo "no tasks selected" >&2; exit 2; fi

# ---- colors (only on a tty) ------------------------------------------------
if [ -t 1 ]; then R=$'\e[31m'; G=$'\e[32m'; B=$'\e[1m'; Z=$'\e[0m'; else R= G= B= Z=; fi

# ---- run -------------------------------------------------------------------
rm -rf "$OUT"
mkdir -p "$LOGS"
echo "${B}Running ${#selected[@]} verification task(s); logs -> $LOGS${Z}"

results=()   # "name|PASS/FAIL|seconds"
overall=0
for entry in "${selected[@]}"; do
  name="${entry%%|*}"; rest="${entry#*|}"; kind="${rest%%|*}"; cmd="${rest#*|}"
  if [ "$kind" = gradle ]; then
    printf '%s──▶ %-14s%s ./gradlew %s\n' "$B" "$name" "$Z" "$cmd"
  else
    printf '%s──▶ %-14s%s %s\n' "$B" "$name" "$Z" "$cmd"
  fi
  start=$SECONDS
  if [ "$kind" = gradle ]; then
    # shellcheck disable=SC2086
    ./gradlew $cmd "${GRADLE_FLAGS[@]}" >"$LOGS/$name.log" 2>&1
  else
    # shellcheck disable=SC2086
    $cmd >"$LOGS/$name.log" 2>&1
  fi
  rc=$?
  dur=$((SECONDS - start))
  if [ "$rc" -eq 0 ]; then
    printf '    %sPASS%s in %ds\n' "$G" "$Z" "$dur"
    results+=("$name|PASS|$dur")
  else
    printf '    %sFAIL%s in %ds (see %s)\n' "$R" "$Z" "$dur" "$LOGS/$name.log"
    results+=("$name|FAIL|$dur")
    overall=1
  fi
done

# ---- collate artifacts -----------------------------------------------------
# Keyed by module path. Prune the vendored sage submodule (separate build) and our own output folder.
echo "${B}Collating artifacts -> $OUT${Z}"
prune=(-path ./sage -prune -o -path "./$OUT" -prune)
ran=" "; for r in "${results[@]}"; do ran+="${r%%|*} "; done   # " name1 name2 … " of tasks that ran

# build/reports from every module — generic (detekt, android-lint, etc. all land here).
find . "${prune[@]}" -o -path '*/build/reports' -type d -print -prune | while read -r d; do
  mod=$(echo "$d" | sed 's|^\./||;s|/build/reports$||;s|/|-|g')
  mkdir -p "$OUT/reports/$mod" && cp -r "$d"/. "$OUT/reports/$mod"/
done
# Paparazzi failure / diff images live under build/paparazzi, not build/reports.
if [[ "$ran" == *" screenshot "* ]]; then
  find . "${prune[@]}" -o -path '*/build/paparazzi' -type d -print -prune | while read -r d; do
    mod=$(echo "$d" | sed 's|^\./||;s|/build/paparazzi$||;s|/|-|g')
    mkdir -p "$OUT/paparazzi/$mod" && cp -r "$d"/. "$OUT/paparazzi/$mod"/
  done
fi
# Debug APK.
if [[ "$ran" == *" apk "* ]] && [ -d apps/android/build/outputs/apk/debug ]; then
  mkdir -p "$OUT/apk"; cp -r apps/android/build/outputs/apk/debug/. "$OUT/apk/"
fi

# ---- summary ---------------------------------------------------------------
summary="$OUT/summary.txt"
{
  echo "Verification summary — $(date '+%Y-%m-%d %H:%M:%S')"
  echo
  printf '  %-14s %-6s %8s\n' "TASK" "RESULT" "TIME"
  printf '  %-14s %-6s %8s\n' "----" "------" "----"
  for r in "${results[@]}"; do
    IFS='|' read -r n s d <<<"$r"
    printf '  %-14s %-6s %7ds\n' "$n" "$s" "$d"
  done
  echo
  if [ "$overall" -eq 0 ]; then echo "OVERALL: PASS"; else echo "OVERALL: FAIL"; fi
  echo
  echo "Artifacts under $OUT/:"
  printf '  %-14s %s\n' "logs/" "full gradle output per task (start here for any FAIL)"
  desc() { [ -d "$OUT/$1" ] && printf '  %-14s %s\n' "$1/" "$2"; }
  desc reports   "ktlint, detekt HTML reports (per module)"
  desc paparazzi "screenshot diff/failure images (per module)"
  desc apk       "debug APK (apps/android)"
} >"$summary"

echo
cat "$summary"
echo
if [ "$overall" -eq 0 ]; then
  echo "${G}${B}✔ all verification tasks passed${Z} — artifacts in $OUT/"
else
  echo "${R}${B}x verification failed${Z} — see $OUT/logs/ and the summary above"
fi
exit "$overall"
