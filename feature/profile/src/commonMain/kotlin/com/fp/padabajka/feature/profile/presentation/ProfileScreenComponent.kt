package com.fp.padabajka.feature.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.feature.profile.domain.DiscardUpdateUseCase
import com.fp.padabajka.feature.profile.domain.ProfileProvider
import com.fp.padabajka.feature.profile.domain.SaveUpdateProfileUseCase
import com.fp.padabajka.feature.profile.domain.update.AboutMeUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.FirstNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.LastNameUpdateUseCase
import com.fp.padabajka.feature.profile.presentation.model.AboutMeUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.DiscardUpdateProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.FirstNameUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.LastNameUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileState
import com.fp.padabajka.feature.profile.presentation.model.SaveUpdateProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.toUIState
import kotlinx.coroutines.launch

class ProfileScreenComponent(
    context: ComponentContext,
    private val profileProvider: ProfileProvider,
    private val discardUpdateUseCase: Factory<DiscardUpdateUseCase>,
    private val saveUpdateProfileUseCase: Factory<SaveUpdateProfileUseCase>,
    private val firstNameUpdateUseCase: Factory<FirstNameUpdateUseCase>,
    private val lastNameUpdateUseCase: Factory<LastNameUpdateUseCase>,
    private val aboutMeUpdateUseCase: Factory<AboutMeUpdateUseCase>
) : BaseComponent<ProfileState>(
    context,
    TODO("add init")
) {

    init {
        componentScope.launch {
            profileProvider.profile.collect { profile ->
                reduce { profile.toUIState() }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            DiscardUpdateProfileEvent -> discardUpdate()
            SaveUpdateProfileEvent -> saveUpdate()
            is FirstNameUpdateEvent -> firstNameUpdate(event)
            is LastNameUpdateEvent -> lastNameUpdate(event)
            is AboutMeUpdateEvent -> aboutMeUpdate(event)
        }
    }

    private fun discardUpdate() =
        mapAndReduceException(
            action = {
                discardUpdateUseCase.get().invoke()
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun saveUpdate() =
        mapAndReduceException(
            action = {
                saveUpdateProfileUseCase.get().invoke()
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun firstNameUpdate(event: FirstNameUpdateEvent) =
        mapAndReduceException(
            action = {
                firstNameUpdateUseCase.get()
                    .invoke(event.firstName)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun lastNameUpdate(event: LastNameUpdateEvent) =
        mapAndReduceException(
            action = {
                lastNameUpdateUseCase.get()
                    .invoke(event.lastName)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun aboutMeUpdate(event: AboutMeUpdateEvent) =
        mapAndReduceException(
            action = {
                aboutMeUpdateUseCase.get()
                    .invoke(event.aboutMe)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )
}
