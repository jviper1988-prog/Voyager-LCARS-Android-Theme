# Voyager LCARS v1.2.1

Fan-made, AI-assisted Android home-screen launcher prototype with matching artwork. Not an official Samsung Galaxy Theme. No APK is included; Android compilation and hardware behavior remain unverified.

Open `preview/Voyager_LCARS_Interactive_v1_2_1.html` in a browser to explore the interface. Native app launching and Android sensors require the built launcher. Preview shortcuts may open other apps or websites. Audio and haptics start off; support varies.

## Build on GitHub
Copy **the contents of this folder**, including `.github`, into the root of your `lcars` repository. Commit and push using GitHub Desktop or an authenticated Git client with write access. GitHub write access was restored on October 27, 2026. The project is being uploaded to jviper1988-prog/lcars; check Actions for build status.

The Actions workflow runs on a push to main/master or manually under Actions → Build Voyager LCARS APK. It installs Java 17, Android SDK 35 and Gradle 8.11.1, then builds and lints. Only a successful run produces the `Voyager-LCARS-installable-debug-APK` artifact. Download and extract it to obtain `app-debug.apk`. Debug signing is intended for personal testing; it is not a production release/signing strategy.

If the build fails, keep the job log for diagnosis. No Gradle wrapper executable/JAR is included. Local builds require an installed compatible Gradle, JDK and Android SDK.

## Phone setup
After a successful build, install the APK from your trusted build output. Select Voyager LCARS in Android’s default Home app settings. To revert, choose One UI Home. Test basic app launches and navigation before using it as your everyday launcher.

Use `theme_assets/Voyager_LCARS_Lock_Screen.png` and `Voyager_LCARS_Home_Wallpaper.png` as artwork. The launcher paints its own background. AOD and icon PNGs are static source art, not installable widgets or icon packs. Good Lock customization is separate and depends on your installed One UI version.

See `TESTED_AND_NOT_TESTED.md` for this review’s results and the remaining phone checklist. `START_HERE.md` retains the supplied v1.2 setup notes with a review banner; prospective feature descriptions there are not verification.

## Local logic checks
With Node installed, run `node tests/ui-logic.cjs` from this folder. These simulate UI logic; they do not test browser layout, Android compilation, or hardware.
