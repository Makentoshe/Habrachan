package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryConverterFactory
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PostByQueryConverterFactoryTest {

    private val factory = GetPostsByQueryConverterFactory()

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `converter should not be null`() {
        assertNotNull(factory.responseBodyConverter(Result.GetPostsByQueryResponse::class.java, arrayOf(), mockk()))
    }
}
