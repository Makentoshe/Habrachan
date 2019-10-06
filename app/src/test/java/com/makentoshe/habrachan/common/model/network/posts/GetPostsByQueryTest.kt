package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQuery
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryConverterFactory
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import retrofit2.Response

class GetPostsByQueryTest {

    @Test
    fun `should return error if response body is null`() {
        val mockedResponse = mockk<Response<Result.GetPostsByQueryResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "{\"data\":null}"
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getPostsByQuery(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetPostsByQuery(mockedHabrApi, GetPostsByQueryConverterFactory())
        val response = login.execute(GetPostsByQueryRequest("", 1, "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 0) { mockedResponse.body() }
        verify(exactly = 1) { mockedResponse.errorBody() }
    }

    @Test
    fun `should return result on success`() {
        val mockedResponse = mockk<Response<Result.GetPostsByQueryResponse>> {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { error } returns null
                every { success } returns mockk()
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getPostsByQuery(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetPostsByQuery(
            mockedHabrApi,
            GetPostsByQueryConverterFactory()
        )
        val response = login.execute(
            GetPostsByQueryRequest(
                "",
                1,
                "",
                ""
            )
        )

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 1) { mockedResponse.body() }
        verify(exactly = 0) { mockedResponse.errorBody() }
    }
}