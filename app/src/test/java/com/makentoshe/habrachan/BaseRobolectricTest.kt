package com.makentoshe.habrachan

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/** Base class for all unit tests works with robolectric framework */
abstract class BaseRobolectricTest : BaseTest() {

    // https://stackoverflow.com/questions/38074224/stub-value-of-build-version-sdk-int-in-local-unit-test/38074424
    protected fun <T> mockBuildConfigField(field: String, value: T) {
        val debugField = BuildConfig::class.java.getField(field)
        debugField.isAccessible = true
        Field::class.java.getDeclaredField("modifiers").also {
            it.isAccessible = true
            it.set(debugField, debugField.modifiers and Modifier.FINAL.inv())
        }
        debugField.set(null, value)
    }
}