package com.makentoshe.habrachan.model.main.login

sealed class OauthType {

    abstract val hostUrl: String

    abstract val socialType: String

    object Github : OauthType() {
        override val hostUrl: String = "https://github.com"
        override val socialType: String = "github"
    }

    companion object {
        fun from(string: String): OauthType = when (string) {
            Github.socialType -> Github
            else -> throw IllegalArgumentException()
        }
    }
}