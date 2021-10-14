package com.makentoshe.habrachan.api.articles.filter

abstract class ArticlesPeriod(val value: String) {
    override fun toString(): String {
        return "Period($value)"
    }
}

fun articlesPeriod(value: String) = object : ArticlesPeriod(value) {}

object DailyArticlesPeriod : ArticlesPeriod("daily")

object WeeklyArticlesPeriod : ArticlesPeriod("weekly")

object MonthlyArticlesPeriod : ArticlesPeriod("monthly")

object YearlyArticlesPeriod : ArticlesPeriod("yearly")

object AlltimeArticlesPeriod : ArticlesPeriod("alltime")


