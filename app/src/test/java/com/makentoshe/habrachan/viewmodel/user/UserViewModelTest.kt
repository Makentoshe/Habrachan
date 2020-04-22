package com.makentoshe.habrachan.viewmodel.user

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.UserDao
import com.makentoshe.habrachan.common.database.session.MeDao
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.User
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

class UserViewModelTest : BaseTest() {

    private lateinit var viewModel: UserViewModel

    private val sessionDao = mockk<SessionDao>()
    private val meDao = mockk<MeDao>()
    private val userDao = mockk<UserDao>()
    private val usersManager = mockk<UsersManager>()
    private val sessionDatabase = mockk<SessionDatabase>()

    @get:Rule
    val timeout = Timeout(15, TimeUnit.SECONDS)

    @Before
    fun before() {
        every { sessionDao.get() } returns session
        every { sessionDatabase.session() } returns sessionDao
        every { sessionDatabase.me() } returns meDao
        viewModel = UserViewModel(usersManager, userDao, sessionDatabase)
    }

    @Test
    fun testShouldReturnSuccessUserResponseForMeAction() {
        // mock for check user was saved in session
        val user = mockk<User>()
        val userResponse = mockk<UserResponse.Success>()
        every { userResponse.user } returns user
        every { meDao.insert(any()) } just runs
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(userResponse)
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
        // check user was saved
        val slot = slot<User>()
        verify(exactly = 1) { meDao.insert(capture(slot)) }
        assertEquals(user, slot.captured)
    }

    @Test
    fun testShouldReturnErrorUserResponseForMeAction() {
        // mock for check user was saved in session
        val userResponse = mockk<UserResponse.Error>()
        // mock for success run request
        every { usersManager.getMe(any()) } returns Single.just(userResponse)
        // perform request
        viewModel.userObserver.onNext(UserAccount.Me)
        val response = viewModel.userObservable.blockingFirst()
        // check is same response
        assertEquals(userResponse, response)
    }

    @Test
    fun testShouldReturnSuccessUserResponseFromCacheForMeAction() {
        val user = mockk<User>()
        every { meDao.get() } returns user
        every { meDao.isEmpty } returns false
        every { meDao.insert(any()) } just runs
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
        every { meDao.isEmpty } returns true
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
        every { userDao.insert(any()) } just runs
        // mock for success run request
        every { usersManager.getUser(any()) } returns Single.just(userResponse)
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