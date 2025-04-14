package com.padabajka.dating.feature.swiper.data

import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.MockFactory
import com.padabajka.dating.core.repository.api.CandidateRepository
import com.padabajka.dating.core.repository.api.MockCandidateRepository
import com.padabajka.dating.core.repository.api.MockNativeAdRepository
import com.padabajka.dating.core.repository.api.MockReactionRepository
import com.padabajka.dating.core.repository.api.NativeAdRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.swiper.AdReaction
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(
    PersonRepository::class,
    NativeAdRepository::class,
    ReactionRepository::class,
    Factory::class,
    CandidateRepository::class
)
class CardRepositoryImplTest {
    private val mocker = Mocker()
    private val candidateRepository: CandidateRepository = MockCandidateRepository(mocker)
    private val nativeAdRepository: NativeAdRepository = MockNativeAdRepository(mocker)
    private val reactionRepository: ReactionRepository = MockReactionRepository(mocker)
    private val cardSelectorFactory: Factory<CardSelector> = MockFactory(mocker)
    private val cardSelectorProvider: CardSelectorProvider =
        CardSelectorProvider(cardSelectorFactory)

    private lateinit var cardRepositoryImpl: CardRepositoryImpl

    @BeforeTest
    fun setUp() {
        cardRepositoryImpl = CardRepositoryImpl(
            candidateRepository,
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
        mocker.everySuspending { candidateRepository.setUsed(PERSON_ID) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            candidateRepository.setUsed(PERSON_ID)
            reactionRepository.react(reaction)
        }
    }

    @Test
    fun testReactPersonReactionDislike() = runBlocking {
        val reaction = PersonReaction.Dislike(PERSON_ID)
        mocker.everySuspending { reactionRepository.react(reaction) } returns Unit
        mocker.everySuspending { candidateRepository.setUsed(PERSON_ID) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            candidateRepository.setUsed(PERSON_ID)
            reactionRepository.react(reaction)
        }
    }

    companion object {
        private val PERSON_ID = PersonId("123")
    }
}
