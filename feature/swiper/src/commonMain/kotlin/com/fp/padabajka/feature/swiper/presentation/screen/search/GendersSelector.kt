package com.fp.padabajka.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fp.padabajka.core.repository.api.model.profile.Gender
import kotlinx.collections.immutable.PersistentList

@Composable
fun GendersSelector(
    genders: PersistentList<Gender>,
    update: (PersistentList<Gender>) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        Gender.entries.forEach { gender ->
            GenderChip(
                gender = gender,
                selected = genders.any { it == gender },
                select = { selected -> update.invoke(genders.add(selected)) },
                unselect = { unselected -> update.invoke(genders.remove(unselected)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GenderChip(
    gender: Gender,
    selected: Boolean,
    select: (Gender) -> Unit,
    unselect: (Gender) -> Unit
) {
    FilterChip(
        onClick = {
            val newSelected = selected.not()
            if (newSelected) {
                select(gender)
            } else {
                unselect(gender)
            }
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Done icon",
                    modifier = Modifier
                )
            }
        },
    ) {
        Text(gender.raw)
    }
}
