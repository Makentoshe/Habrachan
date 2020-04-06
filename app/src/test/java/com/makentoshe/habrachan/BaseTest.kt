package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import java.io.File

abstract class BaseTest {

    protected val session = UserSession(
        "85cab69095196f3.89453480",
        "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        File("token").readText()
    )

    protected fun getJsonResponse(title: String): String {
        val s = File.separator
        val path = "${s}src${s}test${s}java${s}com${s}makentoshe${s}habrachan${s}json${s}$title"
        return File(File("").absoluteFile, path).readText()
    }
}