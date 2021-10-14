package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesFilter

/** hashCode and equals were generated due to [filters] Array type.*/
data class GetArticlesRequest(val parameters: AdditionalRequestParameters, val filters: Array<out ArticlesFilter>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GetArticlesRequest

        if (parameters != other.parameters) return false
        if (!filters.contentEquals(other.filters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parameters.hashCode()
        result = 31 * result + filters.contentHashCode()
        return result
    }
}
