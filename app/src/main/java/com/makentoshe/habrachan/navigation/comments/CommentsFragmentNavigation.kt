package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.user.UserScreen
import ru.terrakok.cicerone.Router

class CommentsFragmentNavigation(private val router: Router) {

    fun back() = router.exit()

    fun toReplyScreen(comments: List<Comment>, articleId: Int) {
        router.navigateTo(CommentsReplyScreen(comments, articleId))
    }

    fun toUserScreen(login: String) {
        router.navigateTo(UserScreen(UserAccount.User(login)))
    }
}