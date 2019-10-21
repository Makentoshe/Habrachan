package com.makentoshe.habrachan.common.repository

import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.InputStream

class RawResourceRepository(private val resources: Resources) : Repository<Int, InputStream> {

    override fun get(@RawRes k: Int): InputStream? {
       return resources.openRawResource(k)
    }
}