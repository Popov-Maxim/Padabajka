package com.padabajka.dating.core.data.utils

import okio.FileSystem

expect object PathUtils {

    val fileSystem: FileSystem

    fun getAbsolutePath(url: String): String
}
