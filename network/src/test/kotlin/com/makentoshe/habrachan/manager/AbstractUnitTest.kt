package com.makentoshe.habrachan.manager

import java.io.File
import java.util.*

// TODO make visible
abstract class AbstractUnitTest {

    private fun File.findParentFileByName(filename: String): File? {
        var file = this
        while (file.parentFile != null) {
            if (file.name == filename) return file
            file = file.parentFile
        }
        return null
    }

    val properties by lazy {
        Properties().apply {
            val root = File(File("").absolutePath).findParentFileByName("Habrachan")
                ?: throw IllegalStateException("Could not find project root")
            load(File(root, "local.properties").inputStream())
        }
    }
}
