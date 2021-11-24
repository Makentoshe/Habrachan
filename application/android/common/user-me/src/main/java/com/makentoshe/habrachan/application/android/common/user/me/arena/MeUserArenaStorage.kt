package com.makentoshe.habrachan.application.android.common.user.me.arena

import android.content.SharedPreferences
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserFromArena
import com.makentoshe.habrachan.functional.Either2
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MeUserArenaStorage(
    private val sharedPreferences: SharedPreferences,
) : ArenaCache3<MeUserArenaRequest, MeUserArenaResponse> {

    override fun fetch(key: MeUserArenaRequest): Either2<MeUserArenaResponse, ArenaStorageException> {
        val json = sharedPreferences.getString(ME_JSON, null) ?: return Either2.Right(EmptyArenaStorageException())
        return Either2.Left(MeUserArenaResponse(key, MeUserFromArena(Json.decodeFromString(json))))
    }

    override fun carry(key: MeUserArenaRequest, value: MeUserArenaResponse) {
        sharedPreferences.edit().putString(ME_JSON, Json.encodeToString(value.me.parameters)).apply()
    }

    companion object : Analytics(LogAnalytic()) {
        internal const val ME_JSON = "MeUserJson"
    }

}