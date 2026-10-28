# Ember Citadel — Android launcher prototype 0.1.0

Original dark fantasy direction; working name pending name/trademark review. Separate app ID `com.jakebrowne.embercitadel`, so it can coexist with Voyager LCARS. This is a native Android launcher, not a web page. It is an early prototype, not a finished paid theme.

## Implemented
- Installed applications from the current Android profile, live app launching, searchable drawer.
- Up to 12 persistent favorites; hold an app to pin/unpin/reorder or inspect App info.
- Home role selection, clock/date/battery, original fortress artwork and geometric crest.
- Optional ember tint on locally installed app icons.
- Phone, Messages, Camera and Web handoffs; themed number pad with editable number, delete, clear, and hold-0 plus sign.
- Norse Bold headings inspired by the angular lettering on Jake's Dragonborn Brewing Co. labels. Small controls use Android sans-serif for readability.

The number pad hands off to the installed Phone app to confirm/place the call. It does not replace the in-call screen, contacts, emergency dialer or call history. Launcher styling does not change Samsung Keyboard, lock screen, AOD, Settings or notification panel.

## Try it
Download and extract the successful workflow's **Ember-Citadel-debug-APK** artifact. Transfer app-debug.apk to the phone and install. Open Ember Citadel, then Settings → Set as default Home. Open Apps and hold applications to pin them. To return to Samsung Home, use Android Settings → Apps → Choose default apps → Home app (wording may vary).

Debug signing is temporary; later builds may require uninstall/reinstall and reset favorites. Do not publish this debug APK as a paid product.

## Build
Java 17, Android SDK 35/build-tools 35.0.0, Gradle 8.11.1. Run `python scripts/fetch_font.py`, then `gradle :app:testDebugUnitTest :app:assembleDebug :app:lintDebug`. The font fetch verifies the original archive checksum. The font is deliberately excluded from source redistribution; the designer license explicitly permits application embedding.

## Evidence and remaining work
See [device checklist](docs/DEVICE_CHECKLIST.md) and [asset sources](docs/ASSET_SOURCES.md). Compiling and automated tests are not proof of Samsung device behavior. No claim of sales, performance improvement or release readiness.
