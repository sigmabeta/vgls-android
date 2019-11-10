# VGLeadSheets for Android

The mobile companion to the website www.vgleadsheets.com. Offers a mobile-tailored experience for viewing sheet music from video games, designed specifically for jam sessions (both the app and the sheets).

## Building

VGLeadSheets should build and install just like any other Android app:

```./gradlew :app:installDebug```

In order to display metadata from the Giant Bomb API, you'll need to get an API key from www.giantbomb.com/api and then run the following:

```APIKEY_GIANTBOMB=\"<your-giant-bomb-key-goes-here>\" >> gradle.properties```

If you deploy the app without a key, it should still work, but you'll get an error message instead of metadata. If you add a key afterwards, clear app data to force metadata to download.

Firebase features are also enabled automatically if you supply a `google-services.json` file.
