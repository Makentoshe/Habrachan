import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.login
import com.makentoshe.habrachan.api.mobile.login.*
import com.makentoshe.habrachan.api.mobile.login.entity.*
import com.makentoshe.habrachan.toRequire
import org.junit.Assert.assertEquals
import org.junit.Test

class HabrLoginApiTest {

    private val api = MobileHabrApi

    @Test
    fun `test should build login post request with Referer header`() {
        val parameters = AdditionalRequestParameters(headers = mapOf("Referer" to "habr.com"))
        val loginAuthBuilder = LoginAuthBuilder {
            email = Email("my-email@custom.com").toRequire()
            password = Password("MyStrongPassword").toRequire()
            state = State("a37e9e4f17d226fe8a2ee57899f5a34d").toRequire()
            consumer = Consumer("habr").toRequire()
            captcha = Captcha("").toRequire()
            googleRecaptchaResponse = GRecaptchaResponse("").toRequire()
            captchaType = CaptchaType("recaptcha").toRequire()
        }
        val request = api.login().auth(loginAuthBuilder.build()).build(parameters)
        assertEquals("https://account.habr.com/ajax/login", request.path)
        assertEquals("email=my-email@custom.com&password=MyStrongPassword&state=a37e9e4f17d226fe8a2ee57899f5a34d&consumer=habr&captcha=&g-recaptcha-response=&captcha_type=recaptcha", request.body)
        assertEquals("habr.com", request.headers["Referer"])
    }

    @Test
    fun `test should build cookies request`() {
        val parameters = AdditionalRequestParameters()
        val request = api.login().cookies().build(parameters)

        assertEquals("https://habr.com/kek/v1/auth/habrahabr", request.path)
    }

}
