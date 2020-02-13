package com.makentoshe.habrachan.model.post.comment

import android.graphics.Bitmap
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit

class ArticleCommentAvatarController(
    private val repository: ArticleCommentAvatarRepository,
    private val disposables: CompositeDisposable,
    private val avatarDao: AvatarDao
) {

    private var url: String = ""

    fun requestAvatar(url: String): ArticleCommentAvatarController {
        this.url = fixUrl(url)
        return this
    }

    fun toAvatarView(viewHolder: ArticleCommentEpoxyModel.ViewHolder) {
        viewHolder.avatarView?.setImageDrawable(null)
        val cachedAvatar = avatarDao.get(File(url).name)
        if (cachedAvatar != null) {
            return setAvatarBitmap(viewHolder, cachedAvatar)
        }
        val context = viewHolder.rootView?.context
        repository.get(url).timeout(30, TimeUnit.SECONDS)
            .map { BitmapController(it).roundCornersPx(context!!, 10) }
            .onErrorReturn {
                context?.resources?.getDrawable(R.drawable.ic_account, context.theme)?.toBitmap()
            }.observeOn(AndroidSchedulers.mainThread()).subscribe({ bitmap ->
                avatarDao.insert(File(url).name, bitmap)
                setAvatarBitmap(viewHolder, bitmap)
            }, {
                viewHolder.progressView?.visibility = View.GONE
                viewHolder.avatarView?.visibility = View.INVISIBLE
            }).let(disposables::add)
    }

    private fun setAvatarBitmap(viewHolder: ArticleCommentEpoxyModel.ViewHolder, bitmap: Bitmap) {
        viewHolder.avatarView?.setImageBitmap(bitmap)
        viewHolder.progressView?.visibility = View.GONE
        viewHolder.avatarView?.visibility = View.VISIBLE
    }

    private fun fixUrl(url: String): String {
        return try {
            return URL(url).toString()
        } catch (e: MalformedURLException) {
            "https:".plus(url)
        }
    }
}