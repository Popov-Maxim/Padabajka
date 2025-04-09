package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

abstract class Dictionary {
    private val words = initDictionary()

    fun getWord(id: StaticTextId): String? {
        return words[id]
    }

    private fun initDictionary(): Map<StaticTextId, String> {
        val uiDictionary = StaticTextId.UiId.entries.associateWith(::getUiWord)
        val assetDictionary = StaticTextId.AssetId.entries.associateWith(::getAssetWord)

        return uiDictionary + assetDictionary
    }

    protected abstract fun getUiWord(uiId: StaticTextId.UiId): String

    protected abstract fun getAssetWord(assetId: StaticTextId.AssetId): String
}
