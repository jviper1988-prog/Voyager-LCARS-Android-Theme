# Voyager LCARS v1.2.2

Fan-made, AI-assisted Android launcher and artwork for personal testing. Not an official Samsung system theme.

This update addresses first-phone feedback: calculator input now has a built-in keypad and Open Keyboard button. The three user-supplied MP3 files replace synthesized sounds. Red Alert uses its own looping audio and mute control; touch audio remains a separate setting.

See CHANGES_v1_2_2.md for changes, audio provenance, and installation/update notes. The v1.2.1 APK compiled and passed Android lint; the user confirmed installation. The v1.2.2 phone fixes require retesting on the S24 Ultra.

## Preview and tests
Open preview/Voyager_LCARS_Interactive_v1_2_2.html with its neighboring audio folder intact. Native app shortcuts and sensors require the APK. Run `node tests/ui-logic.cjs` for simulated UI checks.

## APK
Use the latest successful Build Voyager LCARS APK run under this repository's Actions tab. Download the Voyager-LCARS-installable-debug-APK artifact and extract app-debug.apk. Builds are debug-signed for personal testing; they are not production releases.

Export logs before replacing an installed version. A fresh build can use a different debug certificate, requiring uninstall/reinstall; see the change notes. Choose One UI Home in default Home app settings before uninstalling.

No device operation is guaranteed by compilation or simulated tests. Artwork, preview screenshots, and the included historical test notes are separate from phone evidence.
