package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.MockCardRepository
import com.padabajka.dating.core.repository.api.model.swiper.AdReaction
import com.padabajka.dating.core.repository.api.model.swiper.Reaction
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(CardRepository::class)
class ReactToCardUseCaseTest {
    private val mocker = Mocker()
    private val cardRepository: CardRepository = MockCardRepository(mocker)
    private val reaction: Reaction = AdReaction

    private val reactToCardUseCase = ReactToCardUseCase(cardRepository)

    @BeforeTest
    fun setUp() = runBlocking {
        with(mocker) {
            everySuspending { cardRepository.react(reaction) } returns Unit
        }
    }

    @Test
    fun testReact() = runBlocking {
        reactToCardUseCase.invoke(reaction)

        mocker.verifyWithSuspend { cardRepository.react(reaction) }
    }
}
