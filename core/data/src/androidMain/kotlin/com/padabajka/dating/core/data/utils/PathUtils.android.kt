package com.padabajka.dating.core.data.utils

import android.app.Application
import okio.FileSystem
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual object PathUtils : KoinComponent {

    actual fun getAbsolutePath(url: String): String {
        val application: Application = get()
        return application.filesDir.resolve(url).absolutePath
    }

    actual val fileSystem: FileSystem
        get() = FileSystem.SYSTEM
}
