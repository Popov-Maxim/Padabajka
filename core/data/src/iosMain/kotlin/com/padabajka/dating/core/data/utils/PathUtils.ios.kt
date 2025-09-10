package com.padabajka.dating.core.data.utils

import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual object PathUtils {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getAbsolutePath(url: String): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )

        return requireNotNull(documentDirectory).path + "/$url"
    }

    actual val fileSystem: FileSystem
        get() = FileSystem.SYSTEM
}
