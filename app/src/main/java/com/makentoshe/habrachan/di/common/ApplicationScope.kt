package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.model.post.comment.SpannedFactory
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.di.common.RepositoryScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ApplicationScope

class ApplicationModule(context: Context) : Module()