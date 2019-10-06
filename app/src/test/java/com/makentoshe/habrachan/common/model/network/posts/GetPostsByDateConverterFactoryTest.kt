package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class GetPostsByDateConverterFactoryTest {

    private val factory = GetPostsByDateConverterFactory()

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `converter should not be null`() {
        assertNotNull(factory.responseBodyConverter(Result.GetPostsByDateResponse::class.java, arrayOf(), mockk()))
    }
}