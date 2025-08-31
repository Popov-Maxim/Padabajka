package com.padabajka.dating.core.presentation.ui.dictionary

sealed interface StaticTextId {
    val id: String

    enum class UiId(val rawId: String, prefix: String? = null) : StaticTextId {

        // Headers
        Settings(rawId = "settings"),
        Profile(rawId = "profile"),
        Editor(rawId = "editor"),
        Matches(rawId = "matches"),
        Chats(rawId = "chats"),

        // Setting screen
        Name(rawId = "name"),
        LogOut(rawId = "log_out"),
        General(rawId = "general_error"),

        // Total for Profile
        Male(rawId = "male", prefix = PROFILE_PREFIX),
        Female(rawId = "female", prefix = PROFILE_PREFIX),
        Everyone(rawId = "everyone", prefix = PROFILE_PREFIX),
        Age(rawId = "age", prefix = PROFILE_PREFIX),
        Distance(rawId = "distance", prefix = PROFILE_PREFIX),
        Km(rawId = "km", prefix = PROFILE_PREFIX),
        Bio(rawId = "bio", prefix = PROFILE_PREFIX),
        LookingFor(rawId = "looking_for", prefix = PROFILE_PREFIX),
        BasicInfo(rawId = "basic_info", prefix = PROFILE_PREFIX),

        City(rawId = "city", prefix = PROFILE_PREFIX),
        SexualOrientation(rawId = "sexual_orientation", prefix = PROFILE_PREFIX),
        Education(rawId = "education", prefix = PROFILE_PREFIX),
        Profession(rawId = "profession", prefix = PROFILE_PREFIX),
        Height(rawId = "height", prefix = PROFILE_PREFIX),
        Character(rawId = "character", prefix = PROFILE_PREFIX),
        Language(rawId = "language", prefix = PROFILE_PREFIX),

        CitySearch(rawId = "city_search", prefix = PROFILE_PREFIX),

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
        MessageForTooOld(rawId = "message_for_too_old")

        // -------------- Profile creating

        ;
        override val id: String = listOfNotNull(prefix, rawId, "ui_id")
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

        ;
        override val id: String = rawId
    }

    companion object {

        private const val PROFILE_PREFIX = "profile"

        private val mapUiId = UiId.entries.associateBy(StaticTextId::id)
        private val mapAssetId = AssetId.entries.associateBy(StaticTextId::id)

        fun parse(id: String): StaticTextId? {
            return mapUiId[id] ?: mapAssetId[id]
        }
    }
}
