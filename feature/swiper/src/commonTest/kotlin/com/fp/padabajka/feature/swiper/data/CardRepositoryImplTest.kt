package com.fp.padabajka.feature.swiper.data

import com.fp.padabajka.core.repository.api.MockNativeAdRepository
import com.fp.padabajka.core.repository.api.MockPersonRepository
import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.AdReaction
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.testing.testScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(PersonRepository::class, NativeAdRepository::class)
class CardRepositoryImplTest {
    private val scope: CoroutineScope = testScope()
    private val mocker = Mocker()
    private val personRepository: PersonRepository = MockPersonRepository(mocker)
    private val nativeAdRepository: NativeAdRepository = MockNativeAdRepository(mocker)
    private val personFlow: Flow<Person> = flow {}

    private lateinit var cardRepositoryImpl: CardRepositoryImpl

    @BeforeTest
    fun setUp() {
        with(mocker) {
            every { personRepository.person } returns personFlow
        }

        cardRepositoryImpl = CardRepositoryImpl(
            scope,
            personRepository,
            nativeAdRepository
        )
    }

    @Test
    fun testReactAdReaction() = runBlocking {
        cardRepositoryImpl.react(AdReaction)
    }

    @Test
    fun testReactPersonReactionLike() = runBlocking {
        val reaction = PersonReaction.Like(PERSON_ID)
        mocker.everySuspending { personRepository.react(reaction) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            personRepository.person
            personRepository.react(reaction)
        }
    }

    @Test
    fun testReactPersonReactionDislike() = runBlocking {
        val reaction = PersonReaction.Dislike(PERSON_ID)
        mocker.everySuspending { personRepository.react(reaction) } returns Unit

        cardRepositoryImpl.react(reaction)

        mocker.verifyWithSuspend {
            personRepository.person
            personRepository.react(reaction)
        }
    }

    companion object {
        private val PERSON_ID = PersonId(123)
    }
}
