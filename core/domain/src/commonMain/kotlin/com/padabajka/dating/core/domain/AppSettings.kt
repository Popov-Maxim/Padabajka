package com.padabajka.dating.core.domain

interface AppSettings {
    val host: String?
}

class MutableAppSettings : AppSettings {
    override var host: String? = null
}
