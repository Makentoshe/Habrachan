package com.makentoshe.habrachan.api.articles

sealed class ArticlesVote(val valueInt: Int, val valueString: String) {
    val key = "vote"

    object Up: ArticlesVote(1, "up")

    data class Down(val reason: DownVoteReason): ArticlesVote(-1, "down")
}

sealed class DownVoteReason(val value: Int) {

    /** It’s hardly a tech post */
    object HardlyTechPost: DownVoteReason(1)

    /** Seems more like a commercial to me */
    object CommertialToMe: DownVoteReason(2)

    /** It’s off-topic to Habr */
    object OffTopic: DownVoteReason(3)

    /** A lot of errors and typos in the text */
    object ErrorsAndTyposInText: DownVoteReason(4)

    /** The post is carelessly formatted */
    object CarelessFormatted: DownVoteReason(5)

    /** Prejudice against the author or company */
    object PrejudiceAgainstAuthorOrCompany: DownVoteReason(6)

    /** There is nothing new for me in this post */
    object NothingNewForMe: DownVoteReason(7)

    /** Didn’t understand anything while reading */
    object DidntUnderstandAnything: DownVoteReason(8)

    object Other: DownVoteReason(9)

    object DisagreeWithTheArticle: DownVoteReason(22)
}
