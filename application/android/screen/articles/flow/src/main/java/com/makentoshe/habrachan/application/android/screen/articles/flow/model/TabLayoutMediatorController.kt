package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import android.content.res.Resources
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.application.android.screen.articles.flow.R
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import javax.inject.Inject

class TabLayoutMediatorController @Inject constructor(
    private val resources: Resources,
    private val availableSpecTypes: AvailableSpecTypes,
) {

    fun attach(tabLayout: TabLayout, viewPager2: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager2) { tab, position -> strategy(tab, position) }.attach()
    }

    fun strategy(tab: TabLayout.Tab, position: Int) {
        tab.text = availableSpecTypes[position].title()
    }

    private fun SpecType.title(): String = when (this) {
        is SpecType.All -> resources.getString(R.string.articles_type_all)
        is SpecType.Interesting -> resources.getString(R.string.articles_type_interesting)
        is SpecType.Top -> when (this.type) {
            is TopSpecType.Alltime -> resources.getString(R.string.articles_top_type_alltime)
            is TopSpecType.Yearly -> resources.getString(R.string.articles_top_type_yearly)
            is TopSpecType.Monthly -> resources.getString(R.string.articles_top_type_monthly)
            is TopSpecType.Weekly -> resources.getString(R.string.articles_top_type_weekly)
            is TopSpecType.Daily -> resources.getString(R.string.articles_top_type_daily)
            else -> resources.getString(R.string.not_implemented)
        }.let { resources.getString(R.string.articles_top_preposition, it) }
        else -> resources.getString(R.string.not_implemented)
    }

}