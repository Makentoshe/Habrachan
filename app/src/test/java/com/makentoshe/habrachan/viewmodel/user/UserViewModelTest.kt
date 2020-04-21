package com.makentoshe.habrachan.viewmodel.user

import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.database.UserDao
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.response.UserResponse
import com.makentoshe.habrachan.model.user.UserAccount
import io.mockk.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.util.concurrent.TimeUnit

class UserViewModelTest {

    private lateinit var viewModel: UserViewModel

    private val sessionDao = mockk<SessionDao>()
    private val userDao = mockk<UserDao>()
    private val usersManager = mockk<UsersManager>()

    private val userSession = spyk(UserSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY, BuildConfig.TOKEN_KEY))

    @get:Rule
    val timeout = Timeout(15, TimeUnit.SECONDS)

    @Before
    fun before() {
        every { sessionDao.get() } returns userSession
        viewModel = UserViewModel(sessionDao, usersManager, userDao)
    }

    @Test
    fun testShouldReturnSuccessUserResponseForMeAction() {
        // mock for check user was saved in session
        val user = mockk<User>()
        val userResponse = mockk<UserResponse.Success>()
        every { userResponse.user } returns user
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(userResponse)
        every { sessionDao.insert(any()) } just runs
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
        // check user was saved in session
        val slot = slot<UserSession>()
        verify(exactly = 1) { sessionDao.insert(capture(slot)) }
        assertEquals(user, slot.captured.me)
    }

    @Test
    fun testShouldReturnErrorUserResponseForMeAction() {
        // mock for check user was saved in session
        val userResponse = mockk<UserResponse.Error>()
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(userResponse)
//        every { sessionDao.insert(any()) } just runs
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
    }

    @Test
    fun testShouldReturnSuccessUserResponseFromCacheForMeAction() {
        val user = mockk<User>()
        every { userSession.me } returns user
        every { sessionDao.insert(any()) } just runs
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(Unit).map { throw Exception() }
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst() as UserResponse.Success
        // check is same response
        assertEquals(user, response.user)
    }

    @Test
    fun testShouldReturnErrorUserResponseFromCacheForMeAction() {
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(Unit).map { throw Exception() }
        every { sessionDao.insert(any()) } just runs
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst() as UserResponse.Error

        assertEquals(400, response.code)
    }

    @Test
    fun testShouldReturnSuccessUserResponseForUserAction() {
        // mock for check user was saved in session
        val user = mockk<User>()
        val userResponse = mockk<UserResponse.Success>()
        every { userResponse.user } returns user
        // mock for success run request
        every { usersManager.getUser(any()) } returns Single.just(userResponse)
        every { userDao.insert(any()) } just runs
        // perform request
        viewModel.userObserver.onNext(UserAccount.User(""))
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
        // check user was saved in session
        val slot = slot<User>()
        verify(exactly = 1) { userDao.insert(capture(slot)) }
        assertEquals(user, slot.captured)
    }

    @Test
    fun testShouldReturnErrorUserResponseForUserAction() {
        val userResponse = mockk<UserResponse.Error>()
        // mock for success run request
        every { usersManager.getUser(any()) } returns Single.just(userResponse)
        // perform request
        viewModel.userObserver.onNext(UserAccount.User(""))
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
    }

    @Test
    fun testShouldReturnSuccessUserResponseFromCacheForUserAction() {
        val user = mockk<User>()
        every { userDao.insert(any()) } just runs
        every { userDao.getByLogin("login") } returns user
        // mock for success run request
        every { usersManager.getUser(any()) } returns Single.just(Unit).map { throw Exception() }
        // perform request
        viewModel.userObserver.onNext(UserAccount.User("login"))
        val response = viewModel.userObservable.blockingFirst() as UserResponse.Success
        // check is same response
        assertEquals(user, response.user)
    }

    @Test
    fun testShouldReturnErrorUserResponseFromCacheForUserAction() {
        // mock for success run request
        every { usersManager.getUser(any()) } returns Single.just(Unit).map { throw Exception() }
        every { userDao.insert(any()) } just runs
        every { userDao.getByLogin("login") } returns null
        // perform request
        viewModel.userObserver.onNext(UserAccount.User("login"))
        val response = viewModel.userObservable.blockingFirst() as UserResponse.Error

        assertEquals(400, response.code)
    }
}