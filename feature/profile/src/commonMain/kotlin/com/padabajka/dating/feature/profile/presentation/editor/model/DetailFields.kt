package com.padabajka.dating.feature.profile.presentation.editor.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.DetailType
import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

data class DetailFields(
    val supportedDetails: SupportedDetails,
    val otherDetails: PersistentList<Detail>
) {
    @Stable
    val allDetails = supportedDetails.allDetails.addAll(otherDetails)
}

data class SupportedDetails(
    val city: DetailUIItem.AssetFromDb,
    val sexualOrientation: DetailUIItem.Asset,
    val education: DetailUIItem.Asset,
    val profession: DetailUIItem.TextField,
    val height: DetailUIItem.Int,
    val character: DetailUIItem.Asset,
    val language: DetailUIItem.Asset
) {
    @Stable
    val allDetails = listOfNotNull(
        city.toDetail(DetailType.City),
        sexualOrientation.toDetail(DetailType.SexualOrientation),
        education.toDetail(DetailType.Education),
        profession.toDetail(DetailType.Profession),
        height.toDetail(DetailType.Height),
        character.toDetail(DetailType.Character),
        language.toDetail(DetailType.Language)
    ).toPersistentList()
}

sealed interface DetailUIItem {
    data class TextField(val value: String?) : DetailUIItem
    data class Asset(val value: Text?, val possibleAssets: PersistentList<Text>) : DetailUIItem
    data class AssetFromDb(
        val value: Text?,
        val foundedAssets: FoundedAssets,
        val searchItem: SearchItem
    ) : DetailUIItem
    data class Int(val value: kotlin.Int?) : DetailUIItem
}

fun DetailUIItem.toDetail(type: String): Detail? {
    val value = when (this) {
        is DetailUIItem.TextField -> value?.let { Detail.Value.String(it) }
        is DetailUIItem.Asset -> value?.let { Detail.Value.Asset(it) }
        is DetailUIItem.AssetFromDb -> value?.let { Detail.Value.Asset(it) }
        is DetailUIItem.Int -> value?.let { Detail.Value.Centimeter(it.toDouble()) }
    } ?: return null
    return Detail(
        type = type,
        value = value,
        icon = null
    )
}

sealed interface FoundedAssets {
    data object Searching : FoundedAssets
    data class Success(val possibleAssets: PersistentList<String>) : FoundedAssets
}

data class SearchItem(
    val value: String
)

fun List<Detail>.toDetailFields(): DetailFields {
    var city: Detail? = null
    var sexualOrientation: Detail? = null
    var education: Detail? = null
    var profession: Detail? = null
    var height: Detail? = null
    var character: Detail? = null
    var language: Detail? = null

    onEach { detail ->
        when (detail.type) {
            DetailType.City -> city = detail
            DetailType.SexualOrientation -> sexualOrientation = detail
            DetailType.Education -> education = detail
            DetailType.Profession -> profession = detail
            DetailType.Height -> height = detail
            DetailType.Character -> character = detail
            DetailType.Language -> language = detail
            else -> Unit
        }
    }

    val supportedDetails = SupportedDetails(
        city = defaultCityField.fillValue(city),
        sexualOrientation = defaultSexualOrientationField.fillValue(sexualOrientation),
        education = defaultEducationField.fillValue(education),
        profession = defaultProfessionField.fillValue(profession),
        height = defaultHeightField.fillValue(height),
        character = defaultCharacterField.fillValue(character),
        language = defaultLanguageField.fillValue(language),
    )

    val otherDetails = this - supportedDetails.allDetails

    return DetailFields(
        supportedDetails,
        otherDetails.toPersistentList()
    )
}

private fun DetailUIItem.Asset.fillValue(detail: Detail?): DetailUIItem.Asset = run {
    val value = detail?.value as? Detail.Value.Asset ?: return@run this
    copy(value = value.raw)
}

private fun DetailUIItem.TextField.fillValue(detail: Detail?): DetailUIItem.TextField = run {
    val value = detail?.value as? Detail.Value.String ?: return@run this
    copy(value = value.raw)
}

private fun DetailUIItem.AssetFromDb.fillValue(detail: Detail?): DetailUIItem.AssetFromDb = run {
    val value = detail?.value as? Detail.Value.Asset ?: return@run this
    copy(value = value.raw)
}

private fun DetailUIItem.Int.fillValue(detail: Detail?): DetailUIItem.Int = run {
    val value = detail?.value as? Detail.Value.Centimeter ?: return@run this
    copy(value = value.raw.roundToInt())
}

private val defaultCityField = DetailUIItem.AssetFromDb(null, FoundedAssets.Searching, SearchItem(""))

private val defaultSexualOrientationField = DetailUIItem.Asset(
    value = null,
    possibleAssets = persistentListOf(), // TODO fill
)

private val defaultEducationField = DetailUIItem.Asset(
    value = null,
    possibleAssets = persistentListOf(), // TODO fill
)

private val defaultProfessionField = DetailUIItem.TextField(
    value = null
)

private val defaultHeightField = DetailUIItem.Int(
    value = null
)

private val defaultCharacterField = DetailUIItem.Asset(
    value = null,
    possibleAssets = persistentListOf(), // TODO fill
)

private val defaultLanguageField = DetailUIItem.Asset(
    value = null,
    possibleAssets = persistentListOf(), // TODO fill
)
