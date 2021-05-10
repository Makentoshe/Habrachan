rootProject.name = "Habrachan"

include(":library")
include(":library:core")
include(":library:network")

include(":library:network-mobile")
include(":library:network-mobile:network-mobile-test")

include(":library:network-native")
include(":library:network-native:network-native-test")

include(":application")
include(":application:core")

include(":application:android")
include(":application:android:app")
include(":application:android:core")
include("functional")

include(":entity")
include(":entity:entity-mobile")
include(":entity:entity-native")
