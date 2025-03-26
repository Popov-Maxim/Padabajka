package com.padabajka.dating

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
