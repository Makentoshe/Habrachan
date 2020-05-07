# Habrachan

[![Build Status](http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:TestDrive_Habrachan_Build)/statusIcon)](http://teamcity.jetbrains.com/viewType.html?buildTypeId=TestDrive_Habrachan_Build&guest=1)

## How to build

In the root invoke `gradlew assembleDebug` for debug apk and `gradlew assembleRelease` for release apk.

To start instrumented tests invoke  `gradlew connectedAndroidTest` or `gradlew cAT`.

To start unit tests invoke `gradlew test`.

To build the APK and immediately install it on a running emulator or connected device, invoke `gradlew installDebug`.
