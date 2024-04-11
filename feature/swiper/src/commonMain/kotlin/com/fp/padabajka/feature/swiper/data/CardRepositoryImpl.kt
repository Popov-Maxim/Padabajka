package com.fp.padabajka.feature.swiper.data

import com.fp.padabajka.core.repository.api.CardRepository
import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.AdCard
import com.fp.padabajka.core.repository.api.model.swiper.AdReaction
import com.fp.padabajka.core.repository.api.model.swiper.Card
import com.fp.padabajka.core.repository.api.model.swiper.PersonCard
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

class CardRepositoryImpl(
    scope: CoroutineScope,
    private val personRepository: PersonRepository,
    private val nativeAdRepository: NativeAdRepository
) : CardRepository {

    init {
        scope.launch {
            personRepository.person.collectIndexed { index, person ->
                cardChanel.send(PersonCard(person))
                if (index != 0 && index % (AD_NUMBER - 1) == 0) {
                    withTimeoutOrNull(LOADING_AD_TIMEOUT.seconds) {
                        nativeAdRepository.loadNextAd()
                    }?.let { nativeAd ->
                        cardChanel.send(AdCard(nativeAd))
                    }
                }
            }
        }
    }

    override val cardChanel: Channel<Card> = Channel(Channel.UNLIMITED)

    override suspend fun react(reaction: Reaction) {
        when (reaction) {
            is PersonReaction -> personRepository.react(reaction)
            AdReaction -> {
                // TODO
            }
        }
    }

    companion object {
        private const val AD_NUMBER = 20
        private const val LOADING_AD_TIMEOUT = 10
    }
}
