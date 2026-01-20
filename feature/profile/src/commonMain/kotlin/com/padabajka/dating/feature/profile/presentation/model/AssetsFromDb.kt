package com.padabajka.dating.feature.profile.presentation.model

import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.editor.model.FoundedAssets
import com.padabajka.dating.feature.profile.presentation.editor.model.SearchItem
import kotlinx.collections.immutable.PersistentList

data class AssetsFromDb(
    val value: PersistentList<Text>,
    val maxValues: Int?,
    val foundedAssets: FoundedAssets,
    val searchItem: SearchItem
)
