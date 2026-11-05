package com.padabajka.dating

import com.padabajka.dating.core.platform.data.res.ResProvider

object AppResProvider : ResProvider {
    override val googleServerClientId: Int = R.string.default_web_client_id
}
