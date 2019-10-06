package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsConverterFactory
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class VotePostConverterFactoryTest {

    private val factory = VotePostConverterFactory()

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `converter should not be null`() {
        assertNotNull(factory.responseBodyConverter(Result.VotePostResponse::class.java, arrayOf(), mockk()))
    }

}