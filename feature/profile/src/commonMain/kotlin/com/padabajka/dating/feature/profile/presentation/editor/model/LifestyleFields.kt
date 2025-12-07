package com.padabajka.dating.feature.profile.presentation.editor.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import com.padabajka.dating.core.repository.api.model.profile.LifestyleType
import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class LifestyleFields(
    val supportedLifestyles: SupportedLifestyles,
)

data class SupportedLifestyles(
    val smoking: LifestyleField,
    val alcohol: LifestyleField,
    val animals: LifestyleField,
)

data class LifestyleField(
    val value: LifestyleUIItem,
    val possibleValues: PossibleValueForLifestyle
)

data class LifestyleUIItem(
    val value: Text? = null,
    val attributes: PersistentList<Text> = persistentListOf()
)

data class PossibleValueForLifestyle(
    val values: PersistentList<StaticTextId>,
    val attributes: PersistentList<StaticTextId>
)

@Stable
fun SupportedLifestyles.isEmpty(): Boolean {
    return smoking.isEmpty() &&
        alcohol.isEmpty() &&
        animals.isEmpty()
}

fun LifestyleField.isEmpty(): Boolean {
    return value.value == null && value.attributes.isEmpty()
}

fun LifestyleFields.toDomain(): List<Lifestyle> {
    with(this.supportedLifestyles) {
        return listOfNotNull(
            smoking.toDomain(LifestyleType.Smoking),
            alcohol.toDomain(LifestyleType.Alcohol),
            animals.toDomain(LifestyleType.Animals),
        )
    }
}

private fun LifestyleField.toDomain(
    type: String,
): Lifestyle? {
    if (value.value == null && value.attributes.isEmpty()) return null
    return Lifestyle(
        type = type,
        value = value.value,
        attributes = value.attributes.toList()
    )
}

private fun Lifestyle.toUIItem(): LifestyleUIItem {
    return LifestyleUIItem(
        value = value,
        attributes = attributes.toPersistentList()
    )
}

private fun LifestyleField.fillLifestyle(value: Lifestyle?): LifestyleField = run {
    val value = value?.toUIItem() ?: return@run this
    copy(value = value)
}

fun LifestyleField.updateValue(value: Text?): LifestyleField = run {
    val value = this.value.copy(value = value)
    copy(value = value)
}

fun LifestyleField.updateAttributes(attributes: PersistentList<Text>): LifestyleField = run {
    val value = this.value.copy(attributes = attributes)
    copy(value = value)
}

@Stable
fun List<Lifestyle>.toLifestyleFields(): LifestyleFields {
    var smoking: Lifestyle? = null
    var alcohol: Lifestyle? = null
    var animals: Lifestyle? = null

    forEach { lifestyle ->
        when (lifestyle.type) {
            LifestyleType.Smoking -> smoking = lifestyle
            LifestyleType.Alcohol -> alcohol = lifestyle
            LifestyleType.Animals -> animals = lifestyle
            else -> Unit
        }
    }

    val supportedDetails = SupportedLifestyles(
        smoking = defaultSmoking.fillLifestyle(smoking),
        alcohol = defaultAlcohol.fillLifestyle(alcohol),
        animals = defaultAnimals.fillLifestyle(animals)
    )

    return LifestyleFields(
        supportedLifestyles = supportedDetails
    )
}

private val defaultSmoking = LifestyleField(
    value = LifestyleUIItem(),
    possibleValues = PossibleValueForLifestyle(
        values = persistentListOf(
            StaticTextId.AssetId.DontSmoking,
            StaticTextId.AssetId.Rarely,
            StaticTextId.AssetId.InTheCompany,
            StaticTextId.AssetId.Regularly,
        ),
        attributes = persistentListOf(
            StaticTextId.AssetId.Cigarettes,
            StaticTextId.AssetId.Cigars,
            StaticTextId.AssetId.Hookah,
            StaticTextId.AssetId.Vape,
            StaticTextId.AssetId.DisposableVape,
        )
    )
)

private val defaultAlcohol = LifestyleField(
    value = LifestyleUIItem(),
    possibleValues = PossibleValueForLifestyle(
        values = persistentListOf(
            StaticTextId.AssetId.DontDrink,
            StaticTextId.AssetId.Rarely,
            StaticTextId.AssetId.InTheCompany,
            StaticTextId.AssetId.Regularly,
        ),
        attributes = persistentListOf(
            StaticTextId.AssetId.Beer,
            StaticTextId.AssetId.Cocktails,
            StaticTextId.AssetId.Wine,
            StaticTextId.AssetId.Whiskey,
            StaticTextId.AssetId.Rum,
            StaticTextId.AssetId.Tequila,
            StaticTextId.AssetId.Vodka,
        )
    )
)

private val defaultAnimals = LifestyleField(
    value = LifestyleUIItem(),
    possibleValues = PossibleValueForLifestyle(
        values = persistentListOf(
            StaticTextId.AssetId.DontLikeAnimals,
            StaticTextId.AssetId.LikeAnimalsNoPet,
            StaticTextId.AssetId.WantToGetAPet,
            StaticTextId.AssetId.HaveAPet,
            StaticTextId.AssetId.AllergicToAnimals,
        ),
        attributes = persistentListOf(
            StaticTextId.AssetId.Cat,
            StaticTextId.AssetId.Dog,
            StaticTextId.AssetId.Fish,
            StaticTextId.AssetId.Parrot,
            StaticTextId.AssetId.Hamster,
        )
    )
)
