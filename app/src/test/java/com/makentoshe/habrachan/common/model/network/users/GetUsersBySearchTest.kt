package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.HabrApi
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class GetUsersBySearchTest {

    private val response = mockk<Response<GetUsersBySearchResult>>()
    private lateinit var getUsersBySearch: GetUsersBySearch

    @Before
    fun init() {
        val call: Call<GetUsersBySearchResult> = mockk()
        every { call.execute() } returns response

        val habrApi: HabrApi = mockk()
        every { habrApi.getUsersBySearch(allAny(), any()) } returns call

        getUsersBySearch = GetUsersBySearch(habrApi)
    }

    @Test
    fun `should return a result`() {
        val result: GetUsersBySearchResult = mockk()
        every { response.body() } returns result
        getUsersBySearch.execute(GetUsersBySearchRequest()).also {
            assertEquals(result, it)
        }
    }

    @Test
    fun `should return a result with error`() {
        val code = 21345
        val message = "asdfghkjl;htr"

        every { response.body() } returns null
        every { response.code() } returns code
        every { response.errorBody() } returns null
        every { response.message() } returns message

        getUsersBySearch.execute(GetUsersBySearchRequest()).also {
            assertFalse(it.success)
            assertEquals(code, it.data.code)
            assertEquals(message, it.data.message)
            assertNull(it.data.users)
            assertNull(it.data.pages)
        }
    }
}