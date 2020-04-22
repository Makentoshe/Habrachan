package com.makentoshe.habrachan.viewmodel.main.login

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.model.main.login.LoginData
import io.mockk.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.util.concurrent.TimeUnit

class LoginViewModelTest: BaseTest() {

    private lateinit var viewModel: LoginViewModel

    private val sessionDao = mockk<SessionDao>()
    private val loginManager = mockk<LoginManager>()

    @get:Rule
    val timeout = Timeout(15, TimeUnit.SECONDS)

    @Before
    fun before() {
        every { sessionDao.get() } returns session
        viewModel = LoginViewModel(sessionDao, loginManager)
    }

    @Test
    fun testShouldReturnUserSession() {
        val mockkResponse = LoginResponse.Success("a", "b")
        every { loginManager.login(any()) } returns Single.just(mockkResponse)
        every { sessionDao.insert(any()) } just runs
        // invoke request
        viewModel.signInObserver.onNext(LoginData("email", "password"))
        // check response
        val response = viewModel.loginObservable.blockingFirst() as LoginResponse.Success
        assertEquals("a", response.accessToken)
        // check UserSession was saved into database
        val slot = slot<UserSession>()
        verify { sessionDao.insert(capture(slot)) }
        assertEquals(session.apiKey, slot.captured.apiKey)
        assertEquals(session.clientKey, slot.captured.clientKey)
        assertEquals(response.accessToken, slot.captured.tokenKey)
    }

    @Test
    fun testShouldReturnErrorResponse() {
        val errorResponse = LoginResponse.Error(listOf("additional"), 400, "message")
        every { loginManager.login(any()) } returns Single.just(errorResponse)
        //invoke request
        viewModel.signInObserver.onNext(LoginData("email", "password"))
        // check response
        val response = viewModel.loginObservable.blockingFirst()
        assertEquals(response, errorResponse)
    }

}