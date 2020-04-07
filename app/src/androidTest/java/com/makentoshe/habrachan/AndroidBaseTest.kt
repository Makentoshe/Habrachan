package com.makentoshe.habrachan

import androidx.annotation.RawRes
import androidx.test.platform.app.InstrumentationRegistry

abstract class AndroidBaseTest {

    protected val instrumentation = InstrumentationRegistry.getInstrumentation()

    protected fun getRawJsonResource(@RawRes id: Int) : String {
        val jsonResource = instrumentation.targetContext.resources.openRawResource(id)
        return String(jsonResource.readBytes())
    }
}