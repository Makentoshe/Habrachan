package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import java.io.File

abstract class BaseTest {

    protected val session = UserSession(
        "85cab69095196f3.89453480",
        "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        File("token").readText()
    )
}