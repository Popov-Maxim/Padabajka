package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

@Suppress("CyclomaticComplexMethod")
class RuDictionary : Dictionary() {
    @Suppress("LongMethod")
    override fun getUiWord(uiId: StaticTextId.UiId): String {
        return when (uiId) {
            StaticTextId.UiId.Settings -> "Настройки"
            StaticTextId.UiId.Profile -> "Профиль"
            StaticTextId.UiId.Editor -> "Редактор"
            StaticTextId.UiId.Matches -> "Метчи"
            StaticTextId.UiId.Chats -> "Чаты"
            StaticTextId.UiId.Likes -> "Лайки"
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
            StaticTextId.UiId.CityHint -> "Из какого ты города?"
            StaticTextId.UiId.SexualOrientation -> "Ориентация"
            StaticTextId.UiId.SexualOrientationHint -> "Кто тебе нравится?"
            StaticTextId.UiId.Education -> "Образование"
            StaticTextId.UiId.EducationHint -> "Где ты учился?"
            StaticTextId.UiId.Profession -> "Профессия"
            StaticTextId.UiId.ProfessionHint -> "Кем ты работаешь?"
            StaticTextId.UiId.ProfessionSelectorTitle -> "Твоя профессия"
            StaticTextId.UiId.ProfessionSelectorBody -> "Расскажи, чем ты занимаешься"
            StaticTextId.UiId.ProfessionSelectorHint -> "Например: дизайнер, врач, программист..."
            StaticTextId.UiId.Height -> "Рост"
            StaticTextId.UiId.HeightHint -> "Какой у тебя рост?"
            StaticTextId.UiId.Character -> "Характер"
            StaticTextId.UiId.CharacterHint -> "Ты больше интроверт или экстраверт?"
            StaticTextId.UiId.Language -> "Язык"
            StaticTextId.UiId.LanguageHint -> "На каких языках ты говоришь?"
            StaticTextId.UiId.Preview -> "просмотр"
            StaticTextId.UiId.CitySearch -> "Поиск города"
            StaticTextId.UiId.Apply -> "Применить"
            StaticTextId.UiId.Reset -> "Сбросить"
            StaticTextId.UiId.Send -> "Отправить"
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
            StaticTextId.UiId.WhatLookingForTitle -> "Что ты ищешь?"
            StaticTextId.UiId.WhatLookingForBody -> "Ты сможешь поменять это в любое время"
            StaticTextId.UiId.WhatAvatarTitle -> "Как ты выглядишь?"
            StaticTextId.UiId.WhatAvatarBody -> "фото ты сможешь поменять в любое время"
            StaticTextId.UiId.CreateProfileText -> "Мы создаем профиль, пожалуйста подождите..."
            StaticTextId.UiId.GeoPermissionScreenTitle -> "Чтобы искать людей поблизости поделитель своей локацией"
            StaticTextId.UiId.NotificationPermissionScreenTitle ->
                "Включите уведомления, чтобы не пропустить важное сообщение"

            StaticTextId.UiId.FinishScreenTitle ->
                "Все нужные разрешения уже включены \uD83C\uDF89\n\n" +
                    "Можно пользоваться приложением на полную"

            StaticTextId.UiId.SuperLikeTitle -> "Суперлайк"
            StaticTextId.UiId.SuperLikeBody -> "Напиши интересное сообщение, {name} его увидит!"
            StaticTextId.UiId.HintSuperLikeMessage -> "Может, начать с комплимента?"
            StaticTextId.UiId.MessageTitle -> "сообщение:"
            StaticTextId.UiId.SuperLikeCountTitle -> "вам доступно:"
            StaticTextId.UiId.EmptyReactionsScreen -> "Пока тут пусто"
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
