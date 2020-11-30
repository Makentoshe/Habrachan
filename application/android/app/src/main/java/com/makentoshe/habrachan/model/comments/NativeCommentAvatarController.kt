package com.makentoshe.habrachan.model.comments

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.makentoshe.habrachan.application.android.core.ui.ImageViewController
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.viewmodel.comments.AvatarCommentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.net.MalformedURLException
import java.net.URL

class NativeCommentAvatarController(
    private val avatarCommentViewModel: AvatarCommentViewModel,
    private val viewDisposables: CompositeDisposable,
    private val imageView: ImageView
) {

    fun setCommentAvatar(avatarUrl: String, onFinished: (Boolean) -> Unit = {}) {
        avatarCommentViewModel.getAvatarObservable(fixUrl(avatarUrl))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { setCommentAvatarResponse(it, onFinished) }
            .let(viewDisposables::add)
    }

    private fun setCommentAvatarResponse(response: ImageResponse, onFinished: (Boolean) -> Unit) = when (response) {
        is ImageResponse.Success -> {
            setCommentAvatarSuccess(response)
            onFinished.invoke(true)
        }
        is ImageResponse.Error -> {
            setCommentAvatarError()
            onFinished.invoke(false)
        }
    }

    private fun setCommentAvatarError() {
        ImageViewController(imageView).setAvatarStub()
    }

    private fun setCommentAvatarSuccess(response: ImageResponse.Success) = if (response.isStub) {
        ImageViewController(imageView).setAvatarStub()
    } else {
        val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
        imageView.setImageBitmap(bitmap)
    }

    private fun fixUrl(url: String): String {
        return try {
            return URL(url).toString()
        } catch (e: MalformedURLException) {
            "https:".plus(url)
        }
    }

    class Factory(
        private val avatarCommentViewModel: AvatarCommentViewModel,
        private val viewDisposables: CompositeDisposable
    ) {
        fun build(imageView: ImageView): NativeCommentAvatarController {
            return NativeCommentAvatarController(avatarCommentViewModel, viewDisposables, imageView)
        }
    }
}