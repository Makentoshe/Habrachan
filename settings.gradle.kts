rootProject.name = "Habrachan"

include(":library")
include(":library:core")
include(":library:network")
include(":library:network-mobile")

include(":application")
include(":application:core")

include(":application:android")
include(":application:android:app")
include(":application:android:core")
include("library:network-native")
findProject(":library:network-native")?.name = "network-native"
