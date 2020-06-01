package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.user.UserScreen
import ru.terrakok.cicerone.Router

class CommentsScreenNavigation(private val router: Router) {

    fun back() = router.exit()

    fun toReplyScreen(comments: List<Comment>) {
        router.navigateTo(CommentsReplyScreen(comments))
    }

    fun toUserScreen(login: String) {
        router.navigateTo(UserScreen(UserAccount.User(login)))
    }
}