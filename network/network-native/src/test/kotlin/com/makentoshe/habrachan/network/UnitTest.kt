package com.makentoshe.habrachan.network

import java.io.FileNotFoundException

abstract class UnitTest {

    val userSession = userSession("", "")

    fun getResourceString(path: String): String {
        return this::class.java.classLoader.getResource(path)?.readText() ?: throw FileNotFoundException(path)
    }
}