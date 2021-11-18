import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.user
import com.makentoshe.habrachan.api.mobile.user.build
import com.makentoshe.habrachan.api.mobile.user.me
import org.junit.Assert
import org.junit.Test

class HabrUserMeApiTest {

    private val api = MobileHabrApi

    @Test
    fun `test should build me request`() {
        val parameters = AdditionalRequestParameters(headers = mapOf("token" to "asd"))
        val request = api.user().me().build(parameters)

        Assert.assertEquals("https://habr.com/kek/v2/me/", request.path)
    }

}