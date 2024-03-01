package com.fp.padabajka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
