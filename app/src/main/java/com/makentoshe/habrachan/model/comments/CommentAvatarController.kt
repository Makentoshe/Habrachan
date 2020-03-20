package com.makentoshe.habrachan.model.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.entity.ImageResponse
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.ui.ImageViewController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class CommentAvatarController(
    private val disposables: CompositeDisposable,
    private val avatarDao: AvatarDao,
    private val imageManager: ImageManager
) {

    private lateinit var request: ImageRequest

    private fun requestAvatar(url: String) {
        request = ImageRequest(fixUrl(url))
    }

    private fun onImageResponse(response: ImageResponse, holder: CommentEpoxyModel.ViewHolder) = when (response) {
        is ImageResponse.Success -> onImageResponseSuccess(response, holder)
        is ImageResponse.Error -> onImageResponseError(response, holder)
    }

    private fun onImageResponseSuccess(response: ImageResponse.Success, holder: CommentEpoxyModel.ViewHolder) {
        val controller = ImageViewController(holder.avatarView!!)
        // set from resources and return
        if (response.isStub) {
            return controller.setAvatarStub()
        }
        // display avatar
        controller.setAvatarFromByteArray(response.bytes)
        holder.progressView!!.visibility = View.GONE
        holder.avatarView!!.visibility = View.VISIBLE
    }

    private fun onImageResponseError(response: ImageResponse.Error, holder: CommentEpoxyModel.ViewHolder) {
        ImageViewController(holder.avatarView!!).setAvatarStub()
        holder.progressView!!.visibility = View.GONE
        holder.avatarView!!.visibility = View.VISIBLE
    }

    fun toAvatarView(viewHolder: CommentEpoxyModel.ViewHolder) {
        viewHolder.avatarView!!.setImageDrawable(null)
        // if file is stub - just set avatar from resources
        if (File(request.imageUrl).name.contains("stub-user")) {
            return onImageResponseError(ImageResponse.Error(""), viewHolder)
        }
        // get avatar from cache if contains
        avatarDao.get(request.imageUrl)?.let { cached ->
            val byteArray = ByteArrayOutputStream().let {
                cached.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.toByteArray()
            }
            cached.recycle()
            val response = ImageResponse.Success(byteArray, false)
            return onImageResponseSuccess(response, viewHolder)
        }
        // load avatar from net
        imageManager.getImage(request).doOnSuccess { response ->
            if (response is ImageResponse.Success) {
                val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
                avatarDao.insert(request.imageUrl, bitmap)
            }
        }.onErrorReturn {
            ImageResponse.Error(it.toString())
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
            onImageResponse(response, viewHolder)
        }.let(disposables::add)
    }

    private fun fixUrl(url: String): String {
        return try {
            return URL(url).toString()
        } catch (e: MalformedURLException) {
            "https:".plus(url)
        }
    }

    class Factory(
        private val disposables: CompositeDisposable,
        private val avatarDao: AvatarDao,
        private val imageManager: ImageManager
    ) {
        fun build(url: String) = CommentAvatarController(disposables, avatarDao, imageManager).also {
            it.requestAvatar(url)
        }
    }
}