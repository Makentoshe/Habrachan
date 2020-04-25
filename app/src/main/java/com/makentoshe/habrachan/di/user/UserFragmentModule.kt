package com.makentoshe.habrachan.di.user

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.model.user.articles.UserArticlesDataSource
import com.makentoshe.habrachan.model.user.articles.UserArticlesPagedListEpoxyController
import com.makentoshe.habrachan.view.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.user.UserViewModel
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModel
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModelExecutorsProvider
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModelSchedulersProvider
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class UserFragmentModule(fragment: UserFragment) : Module() {

    private val avatarManager: ImageManager
    private val usersManager: UsersManager
    private val articlesManager: ArticlesManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        avatarManager = ImageManager.Builder(client).build()
        usersManager = UsersManager.Builder(client).build()
        articlesManager = ArticlesManager.Builder(client).build()

        bind<UserViewModel>().toInstance(getUserViewModel(fragment))
        bind<UserAvatarViewModel>().toInstance(getUserAvatarViewModel(fragment))
        bind<UserFragment.Navigator>().toInstance(UserFragment.Navigator(router))
        bind<UserArticlesViewModel>().toInstance(getUserArticlesViewModel(fragment))
    }

    private fun getUserAvatarViewModel(fragment: UserFragment): UserAvatarViewModel {
        val application = fragment.requireActivity().application
        val factory = UserAvatarViewModel.Factory(cacheDatabase.avatars(), application, avatarManager)
        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
    }

    private fun getUserViewModel(fragment: UserFragment): UserViewModel {
        val factory = UserViewModel.Factory(cacheDatabase, sessionDatabase, usersManager)
        return ViewModelProviders.of(fragment, factory)[UserViewModel::class.java]
    }

    private fun getUserArticlesViewModel(fragment: UserFragment): UserArticlesViewModel {
        val userArticlesDataSourceFactory = UserArticlesDataSource.Factory(articlesManager, cacheDatabase, sessionDatabase)
        val controller = UserArticlesPagedListEpoxyController(ArticleEpoxyModel.Factory(router))
        val executorsProvider = object : UserArticlesViewModelExecutorsProvider {
            override val fetchExecutor = Executors.newSingleThreadExecutor()
            override val notifyExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }
        }
        val schedulersProvider = object : UserArticlesViewModelSchedulersProvider {
            override val ioScheduler = Schedulers.io()
        }
        val factory = UserArticlesViewModel.Factory(userArticlesDataSourceFactory, controller, executorsProvider, schedulersProvider)
        return ViewModelProviders.of(fragment, factory)[UserArticlesViewModel::class.java]
    }
}
