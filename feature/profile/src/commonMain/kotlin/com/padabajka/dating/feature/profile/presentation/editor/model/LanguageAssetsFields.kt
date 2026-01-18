package com.padabajka.dating.feature.profile.presentation.editor.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.repository.api.model.profile.LanguagesAsset
import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class LanguagesAssetsFields(
    val nativeLanguages: LanguageAssetsField,
    val knownLanguages: LanguageAssetsField,
    val learningLanguages: LanguageAssetsField,
)

data class LanguageAssetsField(
    val value: PersistentList<Text>,
    val maxValues: Int,
    val foundedAssets: FoundedAssets,
    val searchItem: SearchItem
)

@Stable
fun LanguagesAssetsFields.isEmpty(): Boolean {
    return nativeLanguages.isEmpty() &&
        knownLanguages.isEmpty() &&
        learningLanguages.isEmpty()
}

@Stable
fun LanguageAssetsField.isEmpty(): Boolean {
    return value.isEmpty()
}

fun LanguagesAssetsFields.toDomain(): LanguagesAsset {
    return LanguagesAsset(
        native = nativeLanguages.value,
        known = knownLanguages.value,
        learning = learningLanguages.value
    )
}

fun LanguageAssetsField.fillValue(value: List<Text>): LanguageAssetsField {
    return copy(value = value.toPersistentList())
}

@Stable
fun LanguagesAsset.toLanguagesFields(): LanguagesAssetsFields {
    val supportedDetails = LanguagesAssetsFields(
        nativeLanguages = Default.defaultNativeLangField.fillValue(native),
        knownLanguages = Default.defaultKnownLangField.fillValue(known),
        learningLanguages = Default.defaultLearningLangField.fillValue(learning),
    )

    return supportedDetails
}

private object Default {
    private val defaultLangField = LanguageAssetsField(
        value = persistentListOf(),
        maxValues = 0,
        foundedAssets = FoundedAssets.Searching,
        searchItem = SearchItem("")
    )

    val defaultNativeLangField = defaultLangField.copy(maxValues = 2)
    val defaultKnownLangField = defaultLangField.copy(maxValues = 5)
    val defaultLearningLangField = defaultLangField.copy(maxValues = 5)
}
