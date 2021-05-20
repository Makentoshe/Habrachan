rootProject.name = "Habrachan"

include(":functional")

include(":library")
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
include(":application:android:database")
include(":application:android:analytics")

include(":entity")
include(":entity:entity-mobile")
include(":entity:entity-native")
