import com.makentoshe.habrachan.network.userSession
import java.io.File
import java.io.FileNotFoundException
import java.util.Properties

abstract class UnitTest {

    protected val properties by lazy { getLocalProperties(File("").absoluteFile) }

    protected val client: String by lazy { properties.getProperty("client") }
    protected val api: String by lazy { properties.getProperty("api") }
    protected val token: String by lazy { properties.getProperty("token") }

    protected val userSession = userSession("", "")

    fun getResourceString(path: String): String {
        return this::class.java.classLoader.getResource(path)?.readText() ?: throw FileNotFoundException(path)
    }

    private fun getLocalProperties(file: File): Properties {
        val files = file.listFiles()?.map { it.name } ?: emptyList()
        return if (files.contains("local.properties")) Properties().apply {
            load(File(file, "local.properties").inputStream())
        } else {
            getLocalProperties(file.parentFile)
        }
    }
}