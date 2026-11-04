package com.padabajka.dating.feature.profile.presentation.creator.gender.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.PreferredGenderUI
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset

@Composable
fun PreferenceSelector(
    selectedGender: PreferredGenderUI?,
    select: (PreferredGenderUI) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextAsset(
            title = StaticTextId.UiId.WhatPreferenceTitle.translate(),
            body = StaticTextId.UiId.WhatPreferenceBody.translate(),
            modifier = Modifier.fillMaxWidth()
        )
        PreferredGenderSelector(selectedGender = selectedGender, select = select)
    }
}
