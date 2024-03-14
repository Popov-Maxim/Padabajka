package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.domain.replaced
import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import kotlin.coroutines.cancellation.CancellationException

class MainAchievementUpdateUseCase(private val repository: EditProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(achievement: Achievement?) {
        repository.update { profile ->
            if (achievement == null) {
                profile.copy(mainAchievement = null)
            } else {
                val mainAchievement = achievement.copy(hidden = false)
                profile.copy(
                    mainAchievement = mainAchievement,
                    achievements = profile.achievements.replaced(achievement, mainAchievement)
                )
            }
        }
    }
}
