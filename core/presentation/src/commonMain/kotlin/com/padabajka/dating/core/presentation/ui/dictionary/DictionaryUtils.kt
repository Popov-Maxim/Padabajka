package com.padabajka.dating.core.presentation.ui.dictionary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.padabajka.dating.core.repository.api.DictionaryRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import org.koin.compose.rememberCurrentKoinScope

@Composable
fun rememberDictionary(): DictionaryRepository {
    val koinScope = rememberCurrentKoinScope()
    return remember {
        koinScope.getKoin().get()
    }
}

@Composable
fun StaticTextId.translate(lang: Language = defaultLanguage): String {
    return rememberTranslation(this, lang).value
}

@Composable
fun Text.translate(lang: Language = defaultLanguage): String {
    return rememberTranslation(this, lang).value
}

@Composable
private fun rememberTranslation(
    staticTextId: StaticTextId,
    lang: Language = defaultLanguage
): State<String> {
    val dictionary = rememberDictionary()
    val fastTranslate = staticTextId.fastTranslate(lang)

    return produceState(initialValue = fastTranslate, key1 = staticTextId) {
        val type = when (staticTextId) {
            is StaticTextId.UiId -> Text.Type.UI
            is StaticTextId.AssetId -> Text.Type.Default
        }
        dictionary.getText(staticTextId.id, type.raw, lang)?.let {
            value = it
        }
    }
}

@Composable
private fun rememberTranslation(
    text: Text,
    lang: Language = defaultLanguage
): State<String> {
    val dictionary = rememberDictionary()
    val fastTranslate = text.fastTranslate(lang)

    return produceState(initialValue = fastTranslate, key1 = text) {
        val type = text.type
        dictionary.getText(text.id.raw, type.raw, lang)?.let {
            value = it
        }
    }
}

@Composable
private fun Text.fastTranslate(lang: Language = defaultLanguage): String {
    val dictionary = rememberDictionary()

    return dictionary.stableGetText(this.id.raw, lang) ?: this.default ?: "TODO(${this.id.raw})"
}

@Composable
private fun StaticTextId.fastTranslate(lang: Language = defaultLanguage): String {
    val dictionary = rememberDictionary()

    return dictionary.stableGetText(this.id, lang) ?: "TODO(${this.id})"
}

@Stable
private fun DictionaryRepository.stableGetText(id: String, lang: Language): String? {
    return getFastText(id, lang)
}

fun StaticTextId.toText(type: Text.Type): Text {
    return Text(
        id = Text.Id(this.id),
        type = type,
    )
}

private val defaultLanguage = Language.Static.EN
