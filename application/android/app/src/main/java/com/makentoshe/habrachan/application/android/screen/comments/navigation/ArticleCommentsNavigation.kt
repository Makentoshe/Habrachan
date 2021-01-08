package com.makentoshe.habrachan.application.android.screen.comments.navigation

import android.content.Context
import android.widget.Toast
import com.makentoshe.habrachan.R
import ru.terrakok.cicerone.Router

class ArticleCommentsNavigation(private val router: Router) {

    fun back() = router.exit()

    fun toUserScreen(context: Context) {
        // TODO implement navigation to user screen
        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_LONG).show()
    }

    fun toCommentReply(context: Context) {
        // TODO implement navigation to user screen
        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_LONG).show()
    }

}