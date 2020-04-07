package com.makentoshe.habrachan.viewmodel.main.login

import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.login.LoginResponse
import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.model.main.account.login.LoginData
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import io.mockk.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    private val sessionDao = mockk<SessionDao>()
    private val loginManager = mockk<LoginManager>()

    private val userSession = UserSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY, "")

    @Before
    fun before() {
        every { sessionDao.get() } returns userSession
        viewModel = LoginViewModel(sessionDao, loginManager)
    }

    @Test
    fun testShouldReturnUserSession() {
        val response = LoginResponse.Success("a", "b")
        every { loginManager.login(any()) } returns Single.just(response)
        every { sessionDao.insert(any()) } just runs
        // invoke request
        viewModel.signInObserver.onNext(LoginData("email", "password"))
        // check response
        val session = viewModel.loginObservable.blockingFirst()
        assertEquals(userSession.clientKey, session.clientKey)
        assertEquals(userSession.apiKey, session.apiKey)
        assertEquals(response.accessToken, session.tokenKey)
        assertNull(session.me)
        // check UserSession was saved into database
        val slot = slot<UserSession>()
        verify { sessionDao.insert(capture(slot)) }
        assertEquals(session, slot.captured)
    }

    @Test
    fun testShouldReturnErrorResponse() {
        val errorResponse = LoginResponse.Error(listOf("additional"), 400, "message")
        every { loginManager.login(any()) } returns Single.just(errorResponse)
        //invoke request
        viewModel.signInObserver.onNext(LoginData("email", "password"))
        // check response
        val response = viewModel.errorObservable.blockingFirst()
        assertEquals(response, errorResponse)
    }

}