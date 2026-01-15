package com.padabajka.dating.core.presentation.ui.dictionary

sealed interface StaticTextId {
    val id: String

    enum class UiId(val rawId: String) : StaticTextId {

        // Headers
        Settings(rawId = "settings"),
        Profile(rawId = "profile"),
        Editor(rawId = "editor"),
        Matches(rawId = "matches"),
        Chats(rawId = "chats"),
        Likes(rawId = "likes"),

        // Setting screen
        Name(rawId = "name"),
        LogOut(rawId = "log_out"),
        General(rawId = "general_error"),
        Subscription(rawId = "subscription"),
        Notification(rawId = "notification"),
        FAQ(rawId = "faq"),
        FreezeProfile(rawId = "freeze_profile"),

        TermsOfUse(rawId = "terms_of_use"),
        DeleteAccount(rawId = "delete_account"),
//        PrivacyPolicy(rawId = "privacy_policy"),

        // Total for Profile
        Male(rawId = "male"),
        Female(rawId = "female"),
        Everyone(rawId = "everyone"),
        Age(rawId = "age"),
        Distance(rawId = "distance"),
        Km(rawId = "km"),
        Bio(rawId = "bio"),
        LookingFor(rawId = "looking_for"),
        BasicInfo(rawId = "basic_info"),
        Lifestyle(rawId = "lifestyle"),

        City(rawId = "city"),
        CityHint(rawId = "city_hint"),
        SexualOrientation(rawId = "sexual_orientation"),
        SexualOrientationHint(rawId = "sexual_orientation_hint"),
        Education(rawId = "education"),
        EducationHint(rawId = "education_hint"),
        Profession(rawId = "profession"),
        ProfessionHint(rawId = "profession_hint"),
        ProfessionSelectorTitle(rawId = "profession_selector_title"),
        ProfessionSelectorBody(rawId = "profession_selector_body"),
        ProfessionSelectorHint(rawId = "profession_selector_hint"),
        Height(rawId = "height"),
        HeightHint(rawId = "heightHint"),
        Character(rawId = "character"),
        CharacterHint(rawId = "character_hint"),
        Language(rawId = "language"),

        CitySearch(rawId = "city_search"),

        Smoking(rawId = "smoking"),
        SmokingHint(rawId = "smoking_hint"),
        Alcohol(rawId = "alcohol"),
        AlcoholHint(rawId = "alcohol_hint"),
        Animals(rawId = "animals"),
        AnimalsHint(rawId = "animals_hint"),

        YourLifestyle(rawId = "your_lifestyle"),
        ChooseTheAppropriateOne(rawId = "choose_the_appropriate_one"),

        Native(rawId = "native"),
        Known(rawId = "known"),
        Learning(rawId = "learn"),

        YourNativeLanguage(rawId = "your_native_language"),
        YourKnownLanguage(rawId = "your_known_language"),
        YourLearningLanguage(rawId = "your_learning_language"),
        SelectUpToNLanguages(rawId = "select_up_to_n_languages"),
        LanguageSearch(rawId = "language_search"),

        // Profile screen
        OpenProfileEditor(rawId = "open_profile_editor"),
        Preview(rawId = "preview"),

        // Search Pref screen
        Filters(rawId = "filters"),

        // Profile editor

        // --------------

        // Chat screen
        EnterMessage(rawId = "enter_message"),

        // Message popup
        MessagePopupLike(rawId = "message_popup_like"),
        MessagePopupReply(rawId = "message_popup_reply"),
        MessagePopupCopy(rawId = "message_popup_copy"),
        MessagePopupPin(rawId = "message_popup_pin"),
        MessagePopupEdit(rawId = "message_popup_edit"),
        MessagePopupDelete(rawId = "message_popup_delete"),

        // General
        Apply(rawId = "apply"),
        Reset(rawId = "reset"),
        Send(rawId = "send"),
        Add(rawId = "Add"),

        // Profile creating

        WelcomeText(rawId = "welcome_text"),
        CreateProfileBtn(rawId = "create_profile_btn"),
        ContinueBtn(rawId = "continue_btn"),

        HowNameTitle(rawId = "how_name_title"),
        HowNameBody(rawId = "how_name_body"),
        HintForName(rawId = "hint_for_name"),

        WhenBirthdayTitle(rawId = "when_birthday_title"),
        WhenBirthdayBody(rawId = "when_birthday_body"),
        HintForBirthday(rawId = "hint_for_birthday"),
        MessageForTooYoung(rawId = "message_for_too_young"),
        MessageForTooOld(rawId = "message_for_too_old"),

        WhatBiologicalSexTitle(rawId = "what_bio_logical_sex_title"),
        WhatBiologicalSexBody(rawId = "what_bio_logical_sex_body"),
        WhatPreferenceTitle(rawId = "what_preference_title"),
        WhatPreferenceBody(rawId = "what_preference_body"),

        WhatLookingForTitle(rawId = "what_looking_for_title"),
        WhatLookingForBody(rawId = "what_looking_for_body"),

        WhatAvatarTitle(rawId = "what_avatar_title"),
        WhatAvatarBody(rawId = "what_avatar_body"),

        CreateProfileText(rawId = "create_profile_text"),

        // -------------- Profile creating

        // Permission Screen
        GeoPermissionScreenTitle(rawId = "geo_permission_screen_title"),
        NotificationPermissionScreenTitle(rawId = "notification_permission_screen_title"),
        FinishScreenTitle(rawId = "finish_screen_title"),

        // -------------- Permission Screen

        // Super Like Dialog
        SuperLikeTitle(rawId = "super_like_title"),
        SuperLikeBody(rawId = "super_like_body"),
        HintSuperLikeMessage(rawId = "hint_super_like_message"),

        MessageTitle(rawId = "message_title"),
        SuperLikeCountTitle(rawId = "super_like_count_title"),

        // -------------- Super Like Dialog

        // LikesMe
        EmptyReactionsScreen(rawId = "empty_reactions_screen"),
        // -------------- LikesMe
        ;
        override val id: String = listOfNotNull(rawId, "ui_id")
            .joinToString(separator = "_")
    }

    enum class AssetId(val rawId: String) : StaticTextId {
        // looking_for
        NonRomantic(rawId = "non_romantic"),
        LookingForFriends(rawId = "looking_for_friends"),
        PeopleToGoOutWith(rawId = "people_to_go_out_with"),
        LanguageExchange(rawId = "language_exchange"),
        Networking(rawId = "networking"),

        CasualRelationship(rawId = "casual_relationship"),
        Dating(rawId = "dating"),
        Flirt(rawId = "flirt"),
        JustForFun(rawId = "just_for_fun"),

        SeriousRelationship(rawId = "serious_relationship"),
        BuildingFamily(rawId = "building_family"),
        // ------------------ looking_for

        // Sexual Orientation
        Heterosexual(rawId = "heterosexual"),
        Homosexual(rawId = "homosexual"),
        Bisexual(rawId = "bisexual"),
        // ------------------ Sexual Orientation

        // Character
        Extrovert(rawId = "extrovert"),
        Introvert(rawId = "introvert"),
        Ambidextrous(rawId = "ambidextrous"),
        // ------------------ Character

        // Total lifestyle
        Rarely(rawId = "rarely"),
        InTheCompany(rawId = "in_the_company"),
        Regularly(rawId = "regularly"),
        // ------------------ Smoking lifestyle

        // Smoking lifestyle
        DontSmoking(rawId = "dont_smoking"),
        Cigarettes("cigarettes"),
        Cigars("cigars"),
        Hookah("hookah"),
        Vape("vape"),
        DisposableVape("disposable_vape"),
        // ------------------ Smoking lifestyle

        // Alcohol lifestyle
        DontDrink(rawId = "dont_drink"),
        Wine("wine"),
        Whiskey("whiskey"),
        Beer("beer"),
        Vodka("vodka"),
        Rum("rum"),
        Cocktails("cocktails"),
        Tequila("tequila"),
        // ------------------ Alcohol lifestyle

        // Animals lifestyle
        DontLikeAnimals(rawId = "dont_like_animals"),
        LikeAnimalsNoPet(rawId = "like_animals_no_pet"),
        WantToGetAPet(rawId = "want_to_get_a_pet"),
        HaveAPet(rawId = "have_a_pet"),
        AllergicToAnimals(rawId = "allergic_to_animals"),

        Cat("cat"),
        Dog("dog"),
        Fish("fish"),
        Parrot("parrot"),
        Hamster("hamster"),
        // ------------------ Animals lifestyle

        ;
        override val id: String = rawId
    }

    companion object {

        private val mapUiId = UiId.entries.associateBy(StaticTextId::id)
        private val mapAssetId = AssetId.entries.associateBy(StaticTextId::id)

        fun parse(id: String): StaticTextId? {
            return mapUiId[id] ?: mapAssetId[id]
        }
    }
}
