package com.makentoshe.habrachan.application.android.common.usersession

import androidx.core.util.Supplier
import javax.inject.Provider

interface AndroidUserSessionProvider : Provider<AndroidUserSession?>, Supplier<AndroidUserSession?>