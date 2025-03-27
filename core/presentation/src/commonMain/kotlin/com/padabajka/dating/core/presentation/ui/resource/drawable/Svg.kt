package com.padabajka.dating.core.presentation.ui.resource.drawable

import com.padabajka.dating.core.presentation.ui.resource.CoreRes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import padabajka.core.presentation.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
internal val CoreRes.svg.message_icon: String
    get() = Res.getUri("drawable/navigation_message_icon.svg")

@OptIn(ExperimentalResourceApi::class)
internal val CoreRes.svg.likes_icon: String
    get() = Res.getUri("drawable/navigation_likes_icon.svg")

@OptIn(ExperimentalResourceApi::class)
internal val CoreRes.svg.swiper_icon: String
    get() = Res.getUri("drawable/navigation_swiper_icon.svg")

@OptIn(ExperimentalResourceApi::class)
internal val CoreRes.svg.profile_icon: String
    get() = Res.getUri("drawable/navigation_profile_icon.svg")

@OptIn(ExperimentalResourceApi::class)
internal val CoreRes.svg.search_pref_icon: String
    get() = Res.getUri("drawable/search_pref_icon.svg")
