import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

abstract class RetrofitUnitTest : UnitTest() {

    protected fun mockedResponseBody(string: String): ResponseBody {
        val mockResponseBody = mockk<ResponseBody>()
        every { mockResponseBody.string() } returns string
        return mockResponseBody
    }

    protected fun mockedResponse(
        isSuccessful: Boolean = true,
        code: Int = 200,
        message: String = "Mocked message",
        responseBodyScope: () -> ResponseBody
    ): Response<ResponseBody> {
        val mockResponse = mockk<Response<ResponseBody>>()
        every { mockResponse.isSuccessful } returns isSuccessful
        every { mockResponse.code() } returns code
        every { mockResponse.message() } returns message

        if (isSuccessful) {
            every { mockResponse.body() } returns responseBodyScope()
        } else {
            every { mockResponse.errorBody() } returns responseBodyScope()
        }

        return mockResponse
    }

    protected fun mockedCallResponse(mockResponseScope: () -> Response<ResponseBody>): Call<ResponseBody> {
        val mockCallResponseBody = mockk<Call<ResponseBody>>()
        every { mockCallResponseBody.execute() } returns mockResponseScope()
        return mockCallResponseBody
    }
}