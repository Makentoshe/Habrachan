import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.login
import com.makentoshe.habrachan.api.android.login.*
import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.toRequire
import org.junit.Assert.assertEquals
import org.junit.Test

class HabrLoginApiTest {

    private val api = AndroidHabrApi

    @Test
    fun `test should build login post request`() {
        val parameters = AdditionalRequestParameters(headers = mapOf("token" to "asd"))
        val loginAuthBuilder = LoginAuthBuilder {
            email = Email("my-email@custom.com").toRequire()
            password = Password("MyStrongPassword").toRequire()
            clientSecret = ClientSecret("ClientSecret41ce").toRequire()
            clientId = ClientId("ClientId.9432").toRequire()
            grantType = GrantType("password").toRequire()
        }
        val request = api.login().auth(loginAuthBuilder.build()).build(parameters)

        assertEquals("https://habr.com/auth/o/access-token", request.path)
        assertEquals(
            "email=my-email@custom.com&password=MyStrongPassword&client_secret=ClientSecret41ce&client_id=ClientId.9432&grant_type=password",
            request.body
        )
        assertEquals("asd", request.headers["token"])
    }
}