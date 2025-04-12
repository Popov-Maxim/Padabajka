package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

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
            StaticTextId.UiId.LookingFor -> "Ищу..."
        }
    }

    override fun getAssetWord(assetId: StaticTextId.AssetId): String {
        return when (assetId) {
            StaticTextId.AssetId.NonRomantic -> "Не роматические"
            StaticTextId.AssetId.LookingForFriends -> "Новые друзья"
            StaticTextId.AssetId.PeopleToGoOutWith -> "Компания для движа"
            StaticTextId.AssetId.LanguageExchange -> "Языковая практика"
            StaticTextId.AssetId.Networking -> "Нетворкинг"

            StaticTextId.AssetId.CasualRelationship -> "Легкие отношения"
            StaticTextId.AssetId.Dating -> "Свидания"
            StaticTextId.AssetId.Flirt -> "Флирт"
            StaticTextId.AssetId.JustForFun -> "просто «развлечься»"

            StaticTextId.AssetId.SeriousRelationship -> "Серьезные отношения"
            StaticTextId.AssetId.BuildingFamily -> "Создания семьи"
        }
    }
}
