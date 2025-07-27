package com.padabajka.dating.core.presentation.ui.dictionary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
    val dictionary = rememberDictionary()

    return dictionary.stableGetText(this.id, lang) ?: TODO()
}

@Composable
fun Text.translate(lang: Language = defaultLanguage): String {
    val dictionary = rememberDictionary()

    return dictionary.stableGetText(this.id.raw, lang) ?: this.default ?: "TODO()"
}

@Stable
private fun DictionaryRepository.stableGetText(id: String, lang: Language): String? {
    return getText(id, lang)
}

fun StaticTextId.toText(): Text {
    return Text(Text.Id(this.id))
}

private val defaultLanguage = Language.Static.EN
