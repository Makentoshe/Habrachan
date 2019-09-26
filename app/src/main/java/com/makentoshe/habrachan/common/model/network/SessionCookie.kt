package com.makentoshe.habrachan.common.model.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.HashMap

interface CookieStorage {

    fun setCookie(url: HttpUrl, cookies: List<Cookie>)

    fun getCookie(url: HttpUrl): List<Cookie>

    fun getCookie(name: String): Cookie?

    fun hasCookie(name: String): Boolean
}

class SessionCookie : CookieStorage, CookieJar {

    private val cookies = HashMap<String, Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            if (this.cookies.containsKey(cookie.name)) {
                this.cookies.remove(cookie.name)
            }
            this.cookies[cookie.name] = cookie
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies.values.toList()
    }

    override fun getCookie(url: HttpUrl): List<Cookie> {
        return loadForRequest(url)
    }

    override fun setCookie(url: HttpUrl, cookies: List<Cookie>) {
        saveFromResponse(url, cookies)
    }

    override fun getCookie(name: String): Cookie? {
        return cookies[name]
    }

    override fun hasCookie(name: String): Boolean {
        return cookies.contains(name)
    }
}