package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import io.mockk.spyk
import java.io.File

/** Base class for all unit tests */
abstract class BaseTest {

    protected val session = spyk(UserSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY, BuildConfig.TOKEN_KEY))

    protected fun getJsonResponse(title: String): String {
        val s = File.separator
        val path = "${s}src${s}test${s}java${s}com${s}makentoshe${s}habrachan${s}json${s}$title"
        return File(File("").absoluteFile, path).readText()
    }
}