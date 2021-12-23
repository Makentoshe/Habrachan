package com.makentoshe.habrachan.network.content.get

import com.makentoshe.habrachan.entity.content.get.NetworkContent
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface GetContentManager {
    suspend fun execute(request: GetContentRequest): Either2<GetContentResponse, GetContentException>
}

class DefaultGetContentManager(private val client: HttpClient) : GetContentManager {

    override suspend fun execute(request: GetContentRequest) = try {
        mapResponse(buildResponse(request).readBytes()).mapLeft { GetContentResponse(request, it) }
    } catch (exception: ClientRequestException) {
        getContentExceptionNetwork(request, exception)
    } catch (exception: Exception) {
        Either2.Right(GetContentException(request, exception))
    }

    private suspend fun buildResponse(request: GetContentRequest): HttpResponse {
        return client.request(Url(request.url)) {
            request.parameters.headers.forEach { (key, value) -> header(key, value) }
            request.parameters.queries.forEach { (key, value) -> parameter(key, value) }
        }
    }

    private fun mapResponse(networkContent: ByteArray): Either2<NetworkContent, GetContentException> {
        return Either2.Left(NetworkContent(networkContent))
    }

    private suspend fun getContentExceptionNetwork(
        request: GetContentRequest,
        exception: ClientRequestException,
    ): Either2.Right<GetContentException> {
        val parameters = getContentExceptionProperties(exception.response.status.value, exception.response.readText())
        return Either2.Right(GetContentException(request, exception, parameters))
    }
}
