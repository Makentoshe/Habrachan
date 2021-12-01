import com.makentoshe.habrachan.network.KtorCookieParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KtorCookieParserTest {

    private val header = "habr_web_redirect_back=%2Fen%2Fall%2F; connect_sid=s%3AOxcCt2I_jOc-eD-JqPi84WOdn9ZVqvTw.W5XDNaqmO1SidAZ%2FFXFA0YbF6YsTANzVpSyCCq12DiU; habrsession_id=habrsession_id_1198fc169bdee6c2b5f60717993675a3; hl=en; fl=en; _ym_uid=1638352303652172195; _ym_d=1638352303; _ym_isad=2; _ym_visorc=w; _ga=GA1.2.279694356.1638352307; _gid=GA1.2.761854827.1638352307"

    @Test
    fun `test should parse header string and return exact cookies amount`() {
        val cookies = KtorCookieParser().parseHeader(header)

        assertEquals(11, cookies.size)
    }

    @Test
    fun `test should parse connect_sid header string `() {
        val cookies = KtorCookieParser().parseHeader(header)
        assertTrue(cookies.any { it.name == "connect_sid" })
    }

}