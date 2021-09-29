package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This entity contains a single search element that uses in articles screen.
 *
 * Most common params that uses in the requests:
 *
 * a.path(require) - contains a path of url for performing a request. This path appends at the base url end.
 * Consumes any string value. After this value only queries locates. For example:
 * https://habr.com/api/v1/posts%2Fall?page=2&include=text_html
 *
 * a.sort(optional)- TODO finish later
 *
 * a.page(optional) - allows to define a specific page. Default articles count in page equals to 50.
 * Consumes any integer value greater than 0 (page counting start from 1). Default value is 1.
 *
 * a.include(optional) - include an additional field in response. For example, in android api when we request
 * an articles list, we can request a "text_html" field. This field represents an article body.
 * Consumes anu string value.
 *
 * a.exclude(optional) - exclude an additional field in response.
 * Consumes anu string value.
 *
 * m.path(require) - contains the path of url for performing a request. This path appends at the base url end.
 * Consumes any string value. After this value only queries locates. For example:
 * https://habr.com/kek/v2/articles/?sort=rating&score=0&page=1
 *
 * m.hl(optional) - defines a habr language. This param defines a habr "framework" language.
 * Possible values: "ru", "en". Default value is "en".
 *
 * m.fl(optional) - define a flow language. This param perform articles filtering by its language.
 * Possible values: "ru", "en", "ru%2Cen". Default value is "en".
 *
 * m.page(optional) - allows to define a specific page. Default articles count in page equals to 50.
 * Consumes any integer value greater than 0 (page counting start from 1). Default value is 1.
 *
 * m.sort(require) - might be requireable.
 * Possible values: rating, date
 *
 * m.score(optional) - requireable when m.sort param is "rating" for filtering articles with selected score values.
 * Consumes any integer value for filtering.
 *
 * m.period(optional) - requireable when m.sort param is "date" for filtering articles with top ratings.
 * Possible values: daily, weekly, monthly, yearly, alltime.
 * */
@Entity
data class ArticlesUserSearchRecord(
    /** The title of this search. */
    @PrimaryKey
    val title: String,

    /** Contains params in most common, type safety and independent way. */
    val params: Map<String, String>,

    /** Is a default search. Default searches can't be deleted in the application. */
    val isDefault: Boolean,
)

val ArticlesUserSearchRecord.androidParams: Map<String, String>
    get() = params.filterKeys { key -> key.startsWith("a") }

val ArticlesUserSearchRecord.androidPath: String
    get() = params["a.path"] ?: ""

val ArticlesUserSearchRecord.androidSort: String
    get() = params["a.sort"] ?: ""

val ArticlesUserSearchRecord.androidPage: String
    get() = params["a.page"] ?: ""

val ArticlesUserSearchRecord.androidInclude: String
    get() = params["a.include"] ?: ""

val ArticlesUserSearchRecord.androidExclude: String
    get() = params["a.exclude"] ?: ""

val ArticlesUserSearchRecord.mobileParams: Map<String, String>
    get() = params.filterKeys { key -> key.startsWith("m") }
