> Review update: v1.2.1 source is included. Read README.md and TESTED_AND_NOT_TESTED.md first. The following v1.2 notes describe intended behavior, not a verified Android release.

# VOYAGER LCARS / Samsung Galaxy S24 Ultra
## Version 1.2 – Android launcher source + themed artwork

This is a fan-made Star Trek: Voyager–era LCARS home-screen launcher project, not an official Samsung Galaxy Theme APK.
The **APK is not included**: a build is required on GitHub Actions (recommended) or Android Studio.
The HTML demo works offline immediately, without an APK, but cannot list/launch Android apps until the launcher is built and installed.

### Included
- `app/` — Android Java launcher hosting the local LCARS console in WebView, with a local JavaScript-to-Android bridge.
- `.github/workflows/build-apk.yml` — cloud build, producing a **debug-signed** APK artifact.
- `app/src/main/assets/index.html` — LCARS console; also playable locally in a browser.
- `theme_assets/` — lock-screen, AOD artwork, home wallpaper, 16 optional LCARS icon images, and color palette.
- Original synthesized LCARS-inspired touch chirps, with separate audio and haptic toggles in System Config.
- `Voyager_LCARS_Wallpaper.png` — previous art variant.

### Feature status
| Feature | Browser demo | APK when built and installed |
|--|--|--|
| Voyager-era interactive LCARS home interface | Yes | Yes |
| Clock, date, calculator, red-alert mode | Yes | Yes |
| Countdown timer while interface is running | Yes | Yes |
| Captain's Log local save/export | Yes | Yes |
| App drawer, installed Android applications | 8 demo shortcuts | Uses Android LauncherApps |
| Open installed applications | Limited to supported browser schemes | Uses LauncherApps startMainActivity |
| Pin 8 favorite apps for LCARS home screen | Demo | Yes (locally saved) |
| Touch chirps and optional haptic taps (off by default) | Audio API / vibration API when supported | Synthesized LCARS-inspired cues and Android vibration |
| Battery percentage | Browser API when supported | Android battery broadcast |
| Set as default Android launcher | No | Yes, via Android settings |
| Change Samsung lock screen, Quick Settings, keyboard | No | No: see Good Lock steps |

### Easiest path to an APK: GitHub Actions
1. Open the empty GitHub repository in GitHub Desktop and clone it to your PC.
2. Extract this package. Copy the contents of the Voyager_LCARS_Launcher folder into the cloned repository root, including the .github directory and workflow. Keep the app, gradle, and other project folders at that root.
3. Copy the standalone Voyager_LCARS_Interactive_v1_2.html preview into the repository's preview folder if you want it alongside the project.
4. In GitHub Desktop, commit the files and push the commit to the main branch.
5. Open the repository's Actions tab, choose Build Voyager LCARS APK, and select Run workflow.
6. When the job succeeds, open its Artifacts and download Voyager-LCARS-installable-debug-APK.
7. Extract the downloaded GitHub artifact archive to find app-debug.apk.
8. Transfer and install that APK on your phone. Enable installation from that file source only if Samsung prompts you and you trust the build. You may need to temporarily change Auto Blocker settings; restore protections afterward.
9. Samsung Settings → Apps → Choose default apps → Home app → Voyager LCARS. Actual wording differs by One UI version.
10. To revert, choose One UI Home as the default Home app. You can uninstall Voyager LCARS normally.

The ChatGPT GitHub connection can read repository content but cannot push files. Use GitHub Desktop or another authenticated Git client for the upload. If the Actions build fails, review the logs and repair the project before installing. Browser behavior has been tested, but Android compilation, on-device launch, and Samsung integration remain unverified.

### Build locally with Android Studio
Install Android Studio and SDK Platform 35, Build Tools 35.0.0, and Java 17. Open the repository as a Gradle project. The GitHub workflow uses Gradle 8.11.1; local builds should use a compatible Gradle version. Run `gradle :app:assembleDebug` in the project root if Gradle is installed, or use Android Studio's Build APK(s) action after successful Gradle sync. There is no checked-in `gradlew` wrapper binary in this project.

### Complete the Samsung-themed experience
**Home screen**: install the built launcher and select it as the default Home app. It provides its own LCARS panels; normal Samsung wallpaper does not show behind the launcher.

**Lock screen**: From Gallery set `theme_assets/Voyager_LCARS_Lock_Screen.png` as lock-screen wallpaper. Reposition Samsung clock above the decorative orange bar, if your One UI version allows. LockStar / ClockFace can further customize if compatible on your version.

**Quick Settings + colors**: Install Samsung **Good Lock** from the Galaxy Store and its **Theme Park** module. In Theme Park, use the included `LCARS_Color_Palette.png`. Choose black background (#000000), amber (#FFB66E), coral (#F29D89), lavender (#C09BCE), violet (#9188C7), light blue (#90BCE3). Theme Park supports Quick Settings and icon/keyboard color changes; exact features vary by phone and One UI release.

**Keyboard**: Use Good Lock > Keys Cafe if compatible, or Theme Park's keyboard theming, to match dark background, lavender and amber highlights.

**AOD**: Included `Voyager_LCARS_AOD_Artwork.png` is *static source art*, not a functioning Always On Display clock/widget. Apply it only if your One UI lock-screen/AOD customization supports custom art; otherwise use a plain black AOD with a similarly colored clock.

**App icons**: 16 original LCARS-inspired optional icon image assets supplied. Import individual replacements only when Theme Park or your preferred custom-icon tool supports them; not an auto-installed icon pack.

**System limitations**: A third-party Android launcher does **not** replace protected Samsung Settings screens, the security lock screen, system notification panel, system dialer, or the S Pen Air Command UI. Good Lock can customize selected One UI surfaces; achieving a pixel-perfect LCARS theme in every Android system screen isn't possible with normal Samsung theme/launcher permissions.

### Privacy and security
- No network, location, contacts, microphone, camera, or storage permission requested by the launcher. It requests Android’s normal VIBRATE permission for the optional haptic toggle. Buttons launch your existing Android apps.
- The WebView loads only the HTML file packaged inside the app and opens supported external URLs in other apps.
- Captain's Log and shortcut preferences are stored locally. Removing app data erases them; export logs before clearing storage.
- Software is not affiliated with or endorsed by Paramount, CBS, Samsung, or the Star Trek franchise.
- Do not download third-party APKs offered under the same name unless you trust the source.
