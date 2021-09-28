rootProject.name = "Habrachan"

include(":functional")

include(":entity")
include(":entity:entity-mobile")
include(":entity:entity-native")

include(":network")

include(":network:network-common")
include(":network:network-common:network-common-content")
include(":network:network-common:network-common-content:network-common-content-test")

include(":network:network-mobile")
include(":network:network-mobile:network-mobile-login")
include(":network:network-mobile:network-mobile-test")

include(":network:network-native")
include(":network:network-native:network-native-common")
include(":network:network-native:network-native-test")
include(":network:network-native:network-native-article-vote")
include(":network:network-native:network-native-article-get")
include(":network:network-native:network-native-article-get:network-native-article-get-test")
include(":network:network-native:network-native-articles-get")
include(":network:network-native:network-native-articles-get:network-native-articles-get-test")
include(":network:network-native:network-native-login")
include(":network:network-native:network-native-login:network-native-login-test")
include(":network:network-native:network-native-comment-vote")
include(":network:network-native:network-native-comment-vote:network-native-comment-vote-test")
include(":network:network-native:network-native-comments-get")
include(":network:network-native:network-native-comments-get:network-native-comments-get-test")
include(":network:network-native:network-native-user-get")
include(":network:network-native:network-native-user-get:network-native-user-get-test")
include(":network:network-native:network-native-user-me")

include(":application")
include(":application:core")
include(":application:common:arena")
include(":application:common:arena:comments")
include(":application:common:arena:content")
include(":application:common:arena:article")
include(":application:common:arena:articles")

include(":application:android")
include(":application:android:app")
include(":application:android:core")
include(":application:android:database")
include(":application:android:analytics")
include(":application:android:navigation")
include(":application:android:di")
include(":application:android:filesystem")
include(":application:android:exception")

include(":application:android:screen")
include(":application:android:screen:comments")
include(":application:android:screen:comments:article")
include(":application:android:screen:comments:thread")
include(":application:android:screen:comments:dispatch")
include(":application:android:screen:articles")
include(":application:android:screen:articles:flow")
include(":application:android:screen:articles:page")

include(":application:android:common")
include(":application:android:common:arena")
include(":application:android:common:avatar")
include(":application:android:common:article")
include(":application:android:common:articles")
include(":application:android:common:comment")
include(":application:android:common:comment:voting")
include(":application:android:common:comment:getting")
include(":application:android:common:comment:posting")
include(":network:network-native:network-native-user-me:network-native-user-me-test")
include("network:network-native:network-native-comment-post")
