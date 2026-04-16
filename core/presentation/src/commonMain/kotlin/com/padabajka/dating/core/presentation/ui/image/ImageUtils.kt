package com.padabajka.dating.core.presentation.ui.image

import com.padabajka.dating.core.repository.api.model.profile.Image

fun Image.raw(): Any? {
    return when (this) {
        is Image.ByteArray -> value
        is Image.Local -> data.rawData
        is Image.Url -> value
    }
}
