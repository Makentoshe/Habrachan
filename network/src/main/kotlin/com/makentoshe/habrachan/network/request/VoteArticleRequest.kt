package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

interface VoteArticleRequest : Request {
    val articleId: ArticleId
    val userSession: UserSession
    val articleVote: ArticleVote
}

sealed class ArticleVote {
    object Up : ArticleVote()
    class Down(val reason: Reason) : ArticleVote() {

        enum class Reason {
            /** It’s hardly a tech post */
            HARDLY_TECH_POST,

            /** Seems more like a commercial to me */
            COMMERTIAL_TO_ME,

            /** It’s off-topic to Habr */
            OFF_TOPIC,

            /** A lot of errors and typos in the text */
            ERRORS_AND_TYPOS_IN_TEXT,

            /** The post is carelessly formatted */
            CARELESS_FORMATTED,

            /** Prejudice against the author or company */
            PREJUDICE_AGAINST_AUTHOR_OR_COMPANY,

            /** There is nothing new for me in this post */
            NOTHING_NEW_FOR_ME,

            /** Didn’t understand anything while reading */
            DIDNT_UNDERSTAND_ANYTHING,

            OTHER,

            /** Disagree with the article */
            DISAGREE_WITH_THE_ARTICLE
        }
    }
}
