rootProject.name = "Habrachan"

include(":functional")

include(":entity")
include(":entity:entity-mobile")
include(":entity:entity-native")

include(":network")

include(":network:network-mobile")
include(":network:network-mobile:network-mobile-login")
include(":network:network-mobile:network-mobile-test")

include(":network:network-native")
include(":network:network-native:network-native-test")
include(":network:network-native:network-native-login")
include(":network:network-native:network-native-login:network-native-login-test")

include(":application")
include(":application:core")

include(":application:android")
include(":application:android:app")
include(":application:android:core")
include(":application:android:database")
include(":application:android:analytics")
