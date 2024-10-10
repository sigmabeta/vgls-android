# fail fast if files not checked in using git lfs
"$HOOKS_DIR"/pre-receive
git lfs install --local
git lfs pull