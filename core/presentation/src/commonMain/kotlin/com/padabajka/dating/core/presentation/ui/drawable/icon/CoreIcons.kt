package com.padabajka.dating.core.presentation.ui.drawable.icon

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.presentation.ui.resource.CoreRes
import com.padabajka.dating.core.presentation.ui.resource.drawable.likes_icon
import com.padabajka.dating.core.presentation.ui.resource.drawable.message_icon
import com.padabajka.dating.core.presentation.ui.resource.drawable.profile_icon
import com.padabajka.dating.core.presentation.ui.resource.drawable.search_pref_icon
import com.padabajka.dating.core.presentation.ui.resource.drawable.swiper_icon
import com.padabajka.dating.core.presentation.ui.resource.painterResource

object CoreIcons {

    object NavigationBar {
        val Messenger
            @Composable
            get() = painterResource(CoreRes.svg.message_icon)

        val Likes
            @Composable
            get() = painterResource(CoreRes.svg.likes_icon)

        val Swiper
            @Composable
            get() = painterResource(CoreRes.svg.swiper_icon)

        val Profile
            @Composable
            get() = painterResource(CoreRes.svg.profile_icon)
    }
    val SearchPref
        @Composable
        get() = painterResource(CoreRes.svg.search_pref_icon)
}
