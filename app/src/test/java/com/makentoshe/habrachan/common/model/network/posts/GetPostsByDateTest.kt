package com.makentoshe.habrachan.common.model.network.posts


import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDate
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateConverterFactory
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import retrofit2.Response

class GetPostsByDateTest {

    @Test
    fun `should return error if response body is null`() {
        val mockedResponse = mockk<Response<Result.GetPostsByDateResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "{}"
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getPostsByDate(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetPostsByDate(mockedHabrApi, GetPostsByDateConverterFactory())
        val response = login.execute(GetPostsByDateRequest("", 1, "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 0) { mockedResponse.body() }
        verify(exactly = 1) { mockedResponse.errorBody() }
    }

    @Test
    fun `should return result on success`() {
        val mockedResponse = mockk<Response<Result.GetPostsByDateResponse>> {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { error } returns null
                every { success } returns mockk()
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getPostsByDate(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetPostsByDate(mockedHabrApi, GetPostsByDateConverterFactory())
        val response = login.execute(GetPostsByDateRequest("", 1, "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 1) { mockedResponse.body() }
        verify(exactly = 0) { mockedResponse.errorBody() }
    }
}
