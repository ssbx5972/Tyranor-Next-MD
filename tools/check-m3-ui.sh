#!/usr/bin/env bash
# Material 3 guard: fail when an upstream merge brings back Miuix or non-Material icons.
set -uo pipefail

fail=0
report=""

# 1) The Miuix dependency must not return.
if hits=$(grep -rn "top\.yukonga" app/src/main/java --include='*.kt' 2>/dev/null); then
  report+="Miuix imports found:"$'\n'"$hits"$'\n\n'
  fail=1
fi
if grep -qE "libs\.miuix" app/build.gradle.kts 2>/dev/null; then
  report+="Miuix dependency declarations found in app/build.gradle.kts"$'\n\n'
  fail=1
fi

# 2) UI icons must be Material Icons. Only engine artwork, the notification
#    small icon, dead legacy composables and the launcher artwork may stay drawables.
allowed=(
  "R.drawable.ic_refresh"            # notification small icon (system requirement)
  "R.drawable.ic_game_add"           # unused legacy floating-nav composable
  "R.drawable.ic_engine_icon"        # engine family artwork fallback
  "R.drawable.ic_about_app_classic"  # launcher artwork shown in About
  "R.drawable.ic_about_app_default"  # launcher artwork shown in About
)
icons=$(grep -rhoE "R\.drawable\.ic_[A-Za-z0-9_]+" app/src/main/java --include='*.kt' 2>/dev/null | sort -u)
new_icons=""
while IFS= read -r icon; do
  [ -z "$icon" ] && continue
  case "$icon" in
    R.drawable.ic_engine_card_*) continue ;;
  esac
  ok=0
  for allow in "${allowed[@]}"; do
    [ "$icon" = "$allow" ] && ok=1
  done
  [ "$ok" -eq 0 ] && new_icons+="$icon"$'\n'
done <<< "$icons"
if [ -n "$new_icons" ]; then
  report+="Non-Material icon drawables found (replace with AppIcons / Material Icons):"$'\n'"$new_icons"$'\n'
  fail=1
fi

if [ "$fail" -ne 0 ]; then
  printf '%s' "$report"
  echo "::error::Material 3 guard failed"
  exit 1
fi

echo "Material 3 guard passed."
