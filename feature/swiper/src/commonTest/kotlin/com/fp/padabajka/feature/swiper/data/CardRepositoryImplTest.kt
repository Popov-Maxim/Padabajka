package com.fp.padabajka.feature.swiper.data

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.MockFactory
import com.fp.padabajka.core.repository.api.MockNativeAdRepository
import com.fp.padabajka.core.repository.api.MockPersonRepository
import com.fp.padabajka.core.repository.api.MockReactionRepository
import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.ReactionRepository
import com.fp.padabajka.core.repository.api.model.swiper.AdReaction
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(PersonRepository::class, NativeAdRepository::class, ReactionRepository::class, Factory::class)
class CardRepositoryImplTest {
    private val mocker = Mocker()
    private val personRepository: PersonRepository = MockPersonRepository(mocker)
    private val nativeAdRepository: NativeAdRepository = MockNativeAdRepository(mocker)
    private val reactionRepository: ReactionRepository = MockReactionRepository(mocker)
    private val cardSelectorFactory: Factory<CardSelector> = MockFactory(mocker)
    private val cardSelectorProvider: CardSelectorProvider =
        CardSelectorProvider(cardSelectorFactory)

    private lateinit var cardRepositoryImpl: CardRepositoryImpl

    @BeforeTest
    fun setUp() {
        cardRepositoryImpl = CardRepositoryImpl(
            personRepository,
            nativeAdRepository,
            reactionRepository,
            cardSelectorProvider
        )
    }

    @Test
    fun testReactAdReaction() = runBlocking {
        val reaction = AdReaction
        mocker.everySuspending { reactionRepository.react(reaction) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            reactionRepository.react(reaction)
        }
    }

    @Test
    fun testReactPersonReactionLike() = runBlocking {
        val reaction = PersonReaction.Like(PERSON_ID)
        mocker.everySuspending { reactionRepository.react(reaction) } returns Unit
        mocker.everySuspending { personRepository.setUsed(PERSON_ID) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            personRepository.setUsed(PERSON_ID)
            reactionRepository.react(reaction)
        }
    }

    @Test
    fun testReactPersonReactionDislike() = runBlocking {
        val reaction = PersonReaction.Dislike(PERSON_ID)
        mocker.everySuspending { reactionRepository.react(reaction) } returns Unit
        mocker.everySuspending { personRepository.setUsed(PERSON_ID) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            personRepository.setUsed(PERSON_ID)
            reactionRepository.react(reaction)
        }
    }

    companion object {
        private val PERSON_ID = PersonId("123")
    }
}
