package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.CasualDating
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.Dating
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.Flirt
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.JustForFun
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.JustMeeting
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.LanguageExchange
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.LookingForFriends
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.Networking
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId.AssetId.PeopleToGoOutWith

@Suppress("CyclomaticComplexMethod")
class RuDictionary : Dictionary() {
    override fun getUiWord(uiId: StaticTextId.UiId): String {
        return when (uiId) {
            StaticTextId.UiId.Settings -> "Настройки"
            StaticTextId.UiId.Profile -> "Профиль"
            StaticTextId.UiId.Editor -> "Редактор"
            StaticTextId.UiId.Name -> "Имя"
            StaticTextId.UiId.LogOut -> "Выйти"
            StaticTextId.UiId.General -> "Общее"
            StaticTextId.UiId.Male -> "Мужчина"
            StaticTextId.UiId.Female -> "Женщина"
            StaticTextId.UiId.Everyone -> "Неважно"
            StaticTextId.UiId.Age -> "Возраст"
            StaticTextId.UiId.Distance -> "Расстояние"
            StaticTextId.UiId.Km -> "км"
            StaticTextId.UiId.Bio -> "Био"
            StaticTextId.UiId.OpenProfileEditor -> "Открыть редактор профиля"
            StaticTextId.UiId.Filters -> "Фильтр"
        }
    }

    override fun getAssetWord(assetId: StaticTextId.AssetId): String {
        return when (assetId) {
            JustMeeting -> "Просто знвкомства"
            LookingForFriends -> "Новые друзья"
            PeopleToGoOutWith -> "Компания для движа"
            LanguageExchange -> "Языковая практика"
            Networking -> "Нетворкинг"
            CasualDating -> "Лёгкие свидания"
            Dating -> "Свидания"
            Flirt -> "Флирт"
            JustForFun -> "просто «развлечься»"
        }
    }
}
