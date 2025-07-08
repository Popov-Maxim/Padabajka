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

        ;
        override val id: String = listOfNotNull(prefix, rawId, "ui_id")
            .joinToString(separator = "_")
    }

    enum class AssetId(override val id: String) : StaticTextId {
        // looking_for
        NonRomantic(id = "non_romantic"),
        LookingForFriends(id = "looking_for_friends"),
        PeopleToGoOutWith(id = "people_to_go_out_with"),
        LanguageExchange(id = "language_exchange"),
        Networking(id = "networking"),

        CasualRelationship(id = "casual_relationship"),
        Dating(id = "dating"),
        Flirt(id = "flirt"),
        JustForFun(id = "just_for_fun"),

        SeriousRelationship(id = "serious_relationship"),
        BuildingFamily(id = "building_family"),
        // ------------------ looking_for
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
