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
            StaticTextId.UiId.Apply -> "Применить"
            StaticTextId.UiId.WelcomeText ->
                "Добро пожаловать в Padabajka " +
                    "мы рады, что вы пресоеденились к нам, давайте познакомимся\n\n" +
                    "Вы можете рассказать о себе создав профиль или сделать это позже и начать пользоваться приложением"

            StaticTextId.UiId.CreateProfileBtn -> "Создать профиль"
            StaticTextId.UiId.ContinueBtn -> "Продолжить"
            StaticTextId.UiId.HowNameTitle -> "Как тебя зовут?"
            StaticTextId.UiId.HowNameBody -> "Лучше указать настоящее имя"
            StaticTextId.UiId.HintForName -> "Имя"
            StaticTextId.UiId.WhenBirthdayTitle -> "Когда ты родился?"
            StaticTextId.UiId.WhenBirthdayBody -> "В профиле будет виден только возраст"
            StaticTextId.UiId.HintForBirthday -> "Твой день рождения"
            StaticTextId.UiId.MessageForTooYoung -> "Извини, регистрация доступна только с 18 лет"
            StaticTextId.UiId.MessageForTooOld -> "Проверьте дату рождения — похоже, вы указали неверный возраст"
            StaticTextId.UiId.WhatBiologicalSexTitle -> "Какой у тебя пол?"
            StaticTextId.UiId.WhatBiologicalSexBody -> "Ты не сможешь поменять его"
            StaticTextId.UiId.WhatPreferenceTitle -> "Кого ты ищешь?"
            StaticTextId.UiId.WhatPreferenceBody -> "Ты сможешь поменять это в любое время"
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

            StaticTextId.AssetId.Heterosexual -> "Гетеросексуал"
            StaticTextId.AssetId.Homosexual -> "Гомосексуал"
            StaticTextId.AssetId.Bisexual -> "Бисексуал"

            StaticTextId.AssetId.Extrovert -> "Экстраверт"
            StaticTextId.AssetId.Introvert -> "Интроверт"
            StaticTextId.AssetId.Ambidextrous -> "Амбидекстр"
        }
    }
}
