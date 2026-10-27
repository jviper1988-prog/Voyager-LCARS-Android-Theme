# v1.2.2 — first phone-feedback update

- User reports successful installation and likes the visual design of v1.2.1. Calculator keyboard did not appear on the phone; sounds did not match the desired Voyager audio.
- Includes the three MP3 files supplied by the user: console beep for buttons, helm console for panel navigation, and looping red-alert audio on activation.
- Red Alert audio is separate from touch sounds, starts enabled, and has a mute control in CONFIG. Deactivating Red Alert or leaving the app stops it. Playback uses device media volume. No synthetic substitute is used.
- Adds an on-screen calculator keypad with digits, arithmetic, parentheses, backspace, clear and equals; adds an Open Keyboard button and explicit Android keyboard request. The input requests a full keyboard so equation operators are available.
- Version code 5 / version 1.2.2. Phone behavior still needs retesting.

The audio was supplied by the user; this review does not establish authorship or redistribution rights. It must not be presented as original sound design in the portfolio.

Upgrade note: GitHub builds currently generate a fresh debug signing key. Android may reject an in-place update. Export Captain's Log first. If Android reports a signature conflict, switch to One UI Home, uninstall the previous test version, and install this version; uninstalling removes saved logs and favorites. A persistent private signing setup is not yet configured.
