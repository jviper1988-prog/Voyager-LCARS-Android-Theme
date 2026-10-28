# Verification and release gaps

## Automated
- Favorites tests cover persistence, ordering, invalid/duplicate entries, capacity, removal and search matching.
- Build and Android lint must pass. See the exact workflow run for results.

## Pending actual Galaxy S24 Ultra testing
- Install side by side with Voyager; select as default Home; Home gesture, Back gesture and restart reliably return here.
- Confirm title lettering matches the intended label feel, fits, and remains legible.
- Open/search/launch installed apps; no-match and uninstall/reinstall behavior.
- Pin, reorder and remove favorites; reopen and reboot to verify persistence.
- Search keyboard opens/closes; no overlap with gesture navigation, camera cutout or larger text.
- Test portrait/landscape and both display resolutions/font scales.
- Dial pad: digits, *, #, long-0 +, insertion/deletion/clear; confirm number in Phone without placing an unwanted call.
- Verify Messages, Camera and Web handoffs with installed handlers.
- Confirm battery/time update and reasonable memory/battery use.
- Capture Home, drawer, dial pad and actual running keyboard screenshots for portfolio evidence. Concept art is not a running-app screenshot.

## Before a paid release
- Decide/implement widgets, folders, work-profile/private-space handling, accessibility polish and configuration backup. These are absent in v0.1.
- Design any additional original icon pack; current app icons come from installed apps and are not bundled commercial icon assets.
- Make separate compatible keyboard/system-theme assets; this launcher does not automatically theme system screens.
- Stable private release signing, update path, store disclosures/privacy policy and target SDK/store-policy review at submission.
- Review chosen name, artwork provenance, licenses and commercial branding. Working name is not legally cleared. AI-generated art is not a guarantee of exclusivity or non-infringement.
- No franchise sounds, symbols, names or Voyager files are in this source tree. Do not import them into a commercial release.
