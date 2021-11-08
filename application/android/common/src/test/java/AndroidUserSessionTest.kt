import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.common.usersession.*
import org.junit.Assert.assertTrue
import org.junit.Test

private const val DEFAULT_CLIENT_ID = "Default client id"
private const val DEFAULT_CLIENT_API = "Default client api"

class AndroidUserSessionTest {

    @Test
    fun `test should check client api provides to request parameters as a header`() {
        `create android user session`().toRequestParameters().`should contain headers` { headers ->
            headers[ClientApi.NAME] == DEFAULT_CLIENT_API
        }
    }

    @Test
    fun `test should check client id provides to request parameters as a header`() {
        `create android user session`().toRequestParameters().`should contain headers` { headers ->
            headers[ClientId.NAME] == DEFAULT_CLIENT_ID
        }
    }

    @Test
    fun `test should check token provides to request parameters as a header`() {
        val tokenValue = "Android token"
        val parameters = `create android user session`(accessToken = tokenValue).toRequestParameters()
        parameters.`should contain headers` { headers ->
            headers[AccessToken.NAME] == tokenValue
        }
    }

    @Test
    fun `test should check habrsessionid provides to request parameters as a cookie`() {
        val habrSessionIdValue = "Habr session id"
        val parameters = `create android user session`(habrSessionId = habrSessionIdValue).toRequestParameters()
        parameters.`should contain cookies` { cookies ->
            cookies[HabrSessionIdCookie.NAME] == habrSessionIdValue
        }
    }

    private fun `create android user session`(
        clientId: String = DEFAULT_CLIENT_ID,
        clientApi: String = DEFAULT_CLIENT_API,
        accessToken: String? = null,
        habrSessionId: String? = null,
    ) = AndroidUserSession(
        client = ClientId(clientId),
        api = ClientApi(clientApi),
        accessToken = accessToken?.let(::AccessToken),
        habrSessionId = habrSessionId?.let(::HabrSessionIdCookie),
    )

    private fun AdditionalRequestParameters.`should contain headers`(action: (Map<String, String>) -> Boolean) {
        assertTrue(action(headers))
    }

    private fun AdditionalRequestParameters.`should contain cookies`(action: (Map<String, String>) -> Boolean) {
        assertTrue(action(cookies))
    }
}