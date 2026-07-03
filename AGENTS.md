# AGENTS.md — VGLeadSheets orientation

Orientation for coding agents. Keep this file lean; update it when a load-bearing
fact below stops being true.

## What VGLeadSheets is

The mobile + desktop companion to www.vgleadsheets.com: browse and view sheet
music (PDF lead sheets) for video-game music, tailored for jam sessions. It fetches
the catalog from the live VGLS API, persists it to a Room database, and renders
sheet PDFs on device. Kotlin Multiplatform with Compose Multiplatform UI, targeting
Android (minSdk 26 / compileSdk 36) and desktop JVM (`:apps:jvm`).

## Architecture at a glance (verified 2026-07-03)

- **DI is Metro** (`dev.zacsweers.metro`), not Hilt (removed). App graphs are
  `@DependencyGraph(AppScope::class)`: `VglsAppGraph` in `apps/android`,
  `JvmVglsGraph` in `apps/jvm`. View-models are contributed with
  `@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())` + `@Inject`
  and resolved in Compose via `metroViewModel<VM>()`. `AppScope` lives in the `sage/`
  submodule (`net.sigmabeta.sage.di.AppScope`, referenced by FQCN). A module that
  owns a `@ContributesIntoMap` VM must be a **direct compile dep of the graph module**
  (Metro only aggregates from the compile classpath) — that's why each app depends on
  every feature module (via `:features:all:di` on Android).

- **Navigation is Voyager** (`cafe.adriel.voyager`), not AndroidX Navigation
  (removed). There is no `NavHost`/`NavBackStackEntry`/`SavedStateHandle`. Screens are
  Voyager `Screen`s; `vgls/android/scaffold`'s `VglsScreens.kt` maps a route string
  (`Destination`) to a `Screen` via `screenForRoute`, and `NavViewModel` drives the
  `Navigator` off the `SageEvent` bus. Screen args are passed to assisted VM factories
  as typed values.

- **KMP source sets**: pure Kotlin → `src/commonMain/kotlin/`. Code that needs `java.*`
  (or is otherwise JVM-bound) → `src/main/java/`, which SAGE's KMP plugin maps to the
  `jvmSharedMain` intermediate source set that both `androidMain` and `jvmMain` depend
  on. Platform-only code → `src/androidMain` / `src/jvmMain` via `expect`/`actual`.
  **Rule: if code CAN be in `commonMain`, it MUST** — verify with
  `compileCommonMainKotlinMetadata`. Converting a module to KMP needs no file moves;
  hoist to `commonMain` incrementally.

- **Module layout** — `<root>:<platform>:<domain>:<layer>`:
  - roots: `:apps:{android,jvm}` (the two entry points), `:vgls:common:*` +
    `:vgls:android:*` (shared library modules), `:features:*` (one per screen).
  - layers: `:api` (interfaces/models/route types, KMP), `:real` (production impl),
    `:fake` (test/alt doubles + desktop stubs), `:di` (Metro `@Provides` wiring —
    `sage.jvm`/`sage.android` + `sage.di`, or `sage.kmp` + `metro` when the wiring must
    aggregate into both graphs). Impls in `:real` are plain classes (no Metro plugin);
    the `:di` module `@Provides` them.
  - Paparazzi screenshot tests live in `:vgls:android:ui:previews:real`, not per
    feature.

- **The `sage/` submodule** provides the base infrastructure every module builds on:
  `AppScope`/DI, coroutines (`SageDispatchers`), logging (`Hatchet`), base UI
  components/strings/icons, the analytics/connectivity/storage/perf/appinfo/time
  interfaces, and the convention Gradle plugins (`sage.kmp`/`sage.android`/`sage.jvm`/
  `sage.di`/`sage.compose.*`). It's referenced by FQCN under `net.sigmabeta.sage.*`.
  It's a git submodule with its own build and version catalog — treat changes to it as
  needing explicit sign-off, and re-run `compileCommonMainKotlinMetadata` after a bump.

## Build / verify (no device, no emulator boot)

```sh
./gradlew :apps:android:assembleDebug   # Android build
./gradlew :apps:jvm:run                 # desktop window (Compose Multiplatform)
./gradlew detekt                        # static analysis
./gradlew ktlintCheck                   # lint (ktlint via the Gradle plugin; reports per module)
./gradlew ktlintFormat                  # auto-fix ktlint findings
./gradlew :vgls:android:ui:previews:real:verifyPaparazziDebug   # screenshot tests
./verify.sh                             # the full CI-mirroring suite (see below)
```

- **`./verify.sh`** runs the same tasks CI does (ktlint, detekt, Paparazzi, a JVM
  shared build, android-lint, debug APK), independently (one failure doesn't stop the
  rest), and collates logs/reports/APK under `build/verification/` with a pass/fail
  summary. Run it before any `git push`, and only push if it reports `OVERALL: PASS`.
  `./verify.sh --list` / `--skip-apps` / `--rerun` / `<task-name>…` scope it.
- Both **detekt and ktlint** gate commit/push — run both (verify.sh does).
- When touching shared code, also compile the JVM target, not just Android:
  `assembleDebug` does **not** build the JVM/desktop target, so an android-only leak in
  `commonMain` slips through. Use `:apps:jvm:classes` (or the module's
  `compileKotlinJvm` / `compileCommonMainKotlinMetadata`).
- Paparazzi: `verifyPaparazziDebug` *checks* the goldens; `recordPaparazziDebug` overwrites
  them (git-LFS). To review a visual change, use **`./paparazzi-diff.sh`** — it re-renders,
  ranks the most-divergent snapshots by AE%, writes `golden | new | diff` montages under
  `build/paparazzi-review/`, and reverts the goldens afterward (non-destructive; pass `--keep`
  once you've decided to re-record). Only re-record when the change is intentional and reviewed.
- Don't boot an AVD to verify — stop after build + lint and hand device testing to the
  user. Don't `git commit` / `git push` unless explicitly asked.

## Never push without (hard gate)

`git push` is forbidden unless **all three** hold — no exceptions, no "it's a tiny change":

1. **`./verify.sh` passes** (`OVERALL: PASS`) — the full CI-mirroring suite (ktlint, detekt,
   Paparazzi, JVM shared build, android-lint, debug APK).
2. **`./paparazzi-diff.sh` has been run and its summary presented to the user for approval** —
   report the most-divergent snapshots (AE%) and whether each change is intended; **the human must
   approve the screenshot changes** (and, if goldens need updating, explicitly OK the re-record —
   never re-record goldens on your own).
3. **The human has explicitly approved the push itself.**

Present the verify.sh result and the paparazzi-diff.sh summary, then wait for the human's go-ahead.
Committing is fine when asked; pushing is gated on the above.
