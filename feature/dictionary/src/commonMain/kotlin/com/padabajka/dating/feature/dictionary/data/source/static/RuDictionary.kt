package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

@Suppress("CyclomaticComplexMethod")
class RuDictionary : Dictionary() {
    override fun getUiWord(uiId: StaticTextId.UiId): String {
        return when (uiId) {
            StaticTextId.UiId.Settings -> "Настройки"
            StaticTextId.UiId.Profile -> "Профиль"
            StaticTextId.UiId.Editor -> "Редактор"
            StaticTextId.UiId.Matches -> "Метчи"
            StaticTextId.UiId.Chats -> "Чаты"
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
            StaticTextId.UiId.BasicInfo -> "Основное"
            StaticTextId.UiId.EnterMessage -> "Введите сообщение..."
            StaticTextId.UiId.MessagePopupLike -> "Лайк"
            StaticTextId.UiId.MessagePopupReply -> "Ответить"
            StaticTextId.UiId.MessagePopupCopy -> "Копировать"
            StaticTextId.UiId.MessagePopupPin -> "Закрепить"
            StaticTextId.UiId.MessagePopupEdit -> "Изменить"
            StaticTextId.UiId.MessagePopupDelete -> "Удалить"
            StaticTextId.UiId.City -> "Город"
            StaticTextId.UiId.SexualOrientation -> "Ориентация"
            StaticTextId.UiId.Education -> "Образование"
            StaticTextId.UiId.Profession -> "Профессия"
            StaticTextId.UiId.Height -> "Рост"
            StaticTextId.UiId.Character -> "Характер"
            StaticTextId.UiId.Language -> "Язык"
            StaticTextId.UiId.Preview -> "просмотр"
            StaticTextId.UiId.CitySearch -> "Поиск города"
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
