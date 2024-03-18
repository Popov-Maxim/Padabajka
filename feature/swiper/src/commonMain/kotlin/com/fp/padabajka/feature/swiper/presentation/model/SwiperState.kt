package com.fp.padabajka.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.repository.api.model.swiper.PersonId

@Immutable
class SwiperState(
    val id: PersonId
) : State
