package com.makentoshe.habrachan.model.main.articles

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.airbnb.epoxy.EpoxyModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import java.util.*

class ArticlesSearchCustomEpoxyModel() : EpoxyModel<ViewGroup>() {

    override fun bind(view: ViewGroup) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.articles_search_element_custom_chipgroup)
        val editText = view.findViewById<TextInputEditText>(R.id.articles_search_element_custom_edittext)
        editText.setOnEditorActionListener { editText, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false
            val selectedChip = chipGroup.findViewById<Chip>(chipGroup.checkedChipId)
            val sort = selectedChip.text.toString().toUpperCase(Locale.getDefault())
            val search = editText.text.toString()
            val spec = GetArticlesRequest.Spec.search(search, GetArticlesRequest.Spec.SearchSortTypes.valueOf(sort))
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, spec)
            return@setOnEditorActionListener true
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_custom
    }

    class Factory {
        fun build(): ArticlesSearchCustomEpoxyModel {
            val model = ArticlesSearchCustomEpoxyModel()
            model.id("custom")
            return model
        }
    }
}