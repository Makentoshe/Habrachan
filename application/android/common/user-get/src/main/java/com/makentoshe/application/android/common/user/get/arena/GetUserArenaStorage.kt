package com.makentoshe.application.android.common.user.get.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.record.UserRecord3
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.get.*
import com.makentoshe.habrachan.functional.Either2
import javax.inject.Inject

class GetUserArenaStorage @Inject constructor(
    private val database: AndroidCacheDatabase,
) : ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> {

    override fun fetch(key: GetUserArenaRequest): Either2<GetUserArenaResponse, ArenaStorageException> = try {
        capture(analyticEvent { "Fetching by key $key" })
        internalFetch(key)
    } catch (exception: Exception) {
        Either2.Right(ArenaStorageException(exception))
    }

    private fun internalFetch(key: GetUserArenaRequest): Either2<GetUserArenaResponse, ArenaStorageException> {
        val record = database.userDao3().getByLogin(key.login.string)
            ?: return Either2.Right(EmptyArenaStorageException())

        capture(analyticEvent { "Fetched $record by key $key" })
        return Either2.Left(GetUserArenaResponse(key, record.toUserFromArena()))
    }

    override fun carry(key: GetUserArenaRequest, value: GetUserArenaResponse) = try {
        capture(analyticEvent { "Carry ${value.user}" })
        database.userDao3().insert(value.user.toUserRecord())
    } catch (exception: Exception) {
        capture(analyticEvent(throwable = exception))
    }

    private fun UserFromArena.toUserRecord() = UserRecord3(
        login = login.value.string,
        fullname = fullname.value.string,
        avatarUrl = avatar.getOrNull()?.string,
        speciality = speciality.value,
        gender = gender.value.int,
        rating = rating.value,
        ratingPosition = ratingPosition.getOrNull(),
        scoresCount = scoreCount.value,
        votesCount = votesCount.value,
        followersCount = followersCount.value,
        followingCount = followingCount.value,
        lastActivityDateTimeRaw = lastActivity.value.string,
        registerDateTimeRaw = registered.value.string,
        birthdayRaw = birthday.getOrNull(),
    )

    private fun UserRecord3.toUserFromArena() = userFromArena(
        login = login,
        fullname = fullname,
        avatarUrl = avatarUrl,
        speciality = speciality,
        gender = gender,
        rating = rating,
        ratingPosition = ratingPosition,
        scoresCount = scoresCount,
        votesCount = votesCount,
        followersCount = followersCount,
        followingCount = followingCount,
        lastActivityDateTimeRaw = lastActivityDateTimeRaw,
        registerDateTimeRaw = registerDateTimeRaw,
        birthdayRaw = birthdayRaw,
    )

    companion object : Analytics(LogAnalytic())

}