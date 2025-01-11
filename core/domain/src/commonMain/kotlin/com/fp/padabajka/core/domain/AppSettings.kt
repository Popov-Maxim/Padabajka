package com.fp.padabajka.core.domain

interface AppSettings {
    val host: String?
}

class MutableAppSettings : AppSettings {
    override var host: String? = null
}
