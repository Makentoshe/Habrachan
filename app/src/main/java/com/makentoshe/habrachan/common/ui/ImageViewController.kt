package com.makentoshe.habrachan.common.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.makentoshe.habrachan.R

class ImageViewController(private val imageView: ImageView) {

    fun setBitmapFromByteArray(byteArray: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(bitmap)
    }

    fun setAvatarFromByteArray(byteArray: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        setAvatarFromBitmap(bitmap)
    }

    fun setAvatarFromBitmap(bitmap: Bitmap) {
        val drawable = RoundedBitmapDrawableFactory.create(imageView.resources, bitmap)
        drawable.cornerRadius = 8f
        imageView.setImageDrawable(drawable)
        imageView.visibility = View.VISIBLE
    }

    fun setAvatarStub() {
        val stub = imageView.resources.getDrawable(R.drawable.ic_account_stub, imageView.context.theme)
        val drawable = RoundedBitmapDrawableFactory.create(imageView.resources, stub.toBitmap())
        drawable.cornerRadius = 8f
        imageView.setImageDrawable(drawable)
        imageView.visibility = View.VISIBLE
    }

    companion object {
        fun from(imageView: ImageView): ImageViewController {
            return ImageViewController(imageView)
        }
    }
}