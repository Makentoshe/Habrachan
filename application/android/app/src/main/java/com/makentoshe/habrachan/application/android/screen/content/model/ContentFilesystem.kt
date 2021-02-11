package com.makentoshe.habrachan.application.android.screen.content.model

import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.makentoshe.habrachan.network.response.ImageResponse

// TODO move to another module
abstract class ContentFilesystem(private val context: Context) {

    protected abstract val permissions: List<String>

    // should save to pictures folder
    fun saveContent(imageResponse: ImageResponse) = Dexter.withContext(context).withPermissions(permissions)
        .withListener(object : BaseMultiplePermissionsListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    permissionGranted(imageResponse)
                } else {
                    // TODO
                }
            }
        }).check()

    protected abstract fun permissionGranted(imageResponse: ImageResponse)
}

