package com.padabajka.dating.feature.profile.domain.update

import com.padabajka.dating.core.domain.replaced
import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import kotlin.coroutines.cancellation.CancellationException

@Deprecated("need delete")
class HideAchievementUseCase(private val repository: DraftProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(achievement: Achievement) {
        repository.update { profile ->
            val hiddenAchievement = achievement.copy(hidden = true)
            profile.copy(
                mainAchievement = profile.mainAchievement?.takeUnless { it == achievement },
                achievements = profile.achievements.replaced(achievement, hiddenAchievement)
            )
        }
    }
}
