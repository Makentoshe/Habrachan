package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.userSession

abstract class NativeUnitTest {
    val client = "85cab69095196f3.89453480"
    val api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b"

    open val userSession = userSession(client, api)
}