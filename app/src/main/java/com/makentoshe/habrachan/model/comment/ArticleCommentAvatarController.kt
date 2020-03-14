package com.makentoshe.habrachan.model.article.comment

import android.graphics.Bitmap
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.ui.BitmapController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class ArticleCommentAvatarController(
    private val repository: ArticleCommentAvatarRepository,
    private val disposables: CompositeDisposable,
    private val avatarDao: AvatarDao
) {

    private var url: String = ""

    private fun requestAvatar(url: String) {
        this.url = fixUrl(url)
    }

    fun toAvatarView(viewHolder: ArticleCommentEpoxyModel.ViewHolder) {
        viewHolder.avatarView?.setImageDrawable(null)
        val context = viewHolder.rootView?.context!!

        if (File(url).name == "stub-user-middle.gif") {
            val avatarStub = context.resources.getDrawable(R.drawable.ic_account_stub, context.theme)!!.toBitmap()
            return setAvatarBitmap(viewHolder, avatarStub)
        }

        val cached = avatarDao.get(url)
        if (cached != null) {
            return setAvatarBitmap(viewHolder, cached)
        }

        repository.get(url).map { BitmapController(it)
            .roundCornersPx(context, 10) }
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ bitmap ->
                avatarDao.insert(url, bitmap)
                setAvatarBitmap(viewHolder, bitmap)
            }, { throwable ->
                viewHolder.progressView?.visibility = View.GONE
                viewHolder.avatarView?.visibility = View.INVISIBLE
            }).let(disposables::add)
    }

    private fun setAvatarBitmap(viewHolder: ArticleCommentEpoxyModel.ViewHolder, bitmap: Bitmap) {
        viewHolder.avatarView!!.setImageBitmap(bitmap)
        viewHolder.progressView!!.visibility = View.GONE
        viewHolder.avatarView!!.visibility = View.VISIBLE
    }

    private fun fixUrl(url: String): String {
        return try {
            return URL(url).toString()
        } catch (e: MalformedURLException) {
            "https:".plus(url)
        }
    }

    class Factory(
        private val repository: ArticleCommentAvatarRepository,
        private val disposables: CompositeDisposable,
        private val avatarDao: AvatarDao
    ) {
        fun build(url: String) = ArticleCommentAvatarController(repository, disposables, avatarDao).also {
            it.requestAvatar(url)
        }
    }
}