package com.padabajka.dating.feature.profile.presentation.creator.gender.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.PreferredGenderUI

@Composable
fun PreferredGenderSelector(
    selectedGender: PreferredGenderUI?,
    select: (PreferredGenderUI) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleChoiceSegmentedButtonRow(
            space = (-25).dp
        ) {
            PreferredGenderSegment(
                selectedGender = selectedGender,
                gender = PreferredGenderUI.Male,
                select = select
            )
            PreferredGenderSegment(
                selectedGender = selectedGender,
                gender = PreferredGenderUI.Female,
                select = select
            )
        }

        SingleChoiceSegmentedButtonRow {
            PreferredGenderSegment(
                selectedGender = selectedGender,
                gender = PreferredGenderUI.Everyone,
                select = select
            )
        }
    }
}

@Composable
private fun SingleChoiceSegmentedButtonRowScope.PreferredGenderSegment(
    selectedGender: PreferredGenderUI?,
    gender: PreferredGenderUI,
    select: (PreferredGenderUI) -> Unit,
) {
    CoreSegmentedButton(
        selected = selectedGender == gender,
        onClick = { select(gender) },
        text = gender.textId
    )
}
