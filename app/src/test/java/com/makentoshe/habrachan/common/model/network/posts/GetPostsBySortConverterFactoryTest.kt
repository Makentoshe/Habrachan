package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.posts.bysort.GetPostsBySortConverterFactory
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GetPostsBySortConverterFactoryTest {

    private val factory = GetPostsBySortConverterFactory()

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `converter should not be null`() {
        assertNotNull(factory.responseBodyConverter(Result.GetPostsBySortResponse::class.java, arrayOf(), mockk()))
    }
}