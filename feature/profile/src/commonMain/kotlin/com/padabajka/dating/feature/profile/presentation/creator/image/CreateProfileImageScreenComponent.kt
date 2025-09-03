package com.padabajka.dating.feature.profile.presentation.creator.image

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.feature.image.domain.GetLocalImageUseCase
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.update.UpdateMainImageUseCase
import com.padabajka.dating.feature.profile.presentation.creator.image.model.CreateProfileImageEvent
import com.padabajka.dating.feature.profile.presentation.creator.image.model.CreateProfileImageState

class CreateProfileImageScreenComponent(
    context: ComponentContext,
    draftProfileProvider: DraftProfileProvider,
    private val updateMainImageUseCase: UpdateMainImageUseCase,
    private val getLocalImageUseCase: GetLocalImageUseCase,
    private val toNext: () -> Unit
) : BaseComponent<CreateProfileImageState>(
    context = context,
    initialState = initState(draftProfileProvider)
) {

    fun onEvent(event: CreateProfileImageEvent) {
        when (event) {
            is CreateProfileImageEvent.ImageSelected -> updateImage(event.image)
            CreateProfileImageEvent.DeleteImage -> updateImage(null)
            CreateProfileImageEvent.Continue -> continueCreating()
        }
    }

    private fun updateImage(image: Image?) = mapAndReduceException(
        action = {
            val uiImage = if (image is Image.Local) {
                getLocalImageUseCase(image)
            } else {
                image
            }
            reduce {
                it.copy(image = uiImage)
            }
        },
        mapper = { TODO(it.toString()) },
        update = { state, _ -> state }
    )

    private fun continueCreating() {
        val image = state.value.image ?: return

        mapAndReduceException(
            action = {
                updateMainImageUseCase(image)
                toNext()
            },
            mapper = { it },
            update = { state, _ -> state }
        )
    }

    private companion object {
        private fun initState(
            draftProfileProvider: DraftProfileProvider
        ): CreateProfileImageState {
            val draftProfile = draftProfileProvider.getProfile()
            val userGender = draftProfile?.mainImage

            return CreateProfileImageState(userGender)
        }
    }
}
