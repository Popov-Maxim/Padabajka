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

        // Setting screen
        Name(rawId = "name"),
        LogOut(rawId = "log_out"),
        General(rawId = "general_error"),

        // Total for Profile
        Male(rawId = "male"),
        Female(rawId = "female"),
        Everyone(rawId = "everyone"),
        Age(rawId = "age"),
        Distance(rawId = "distance"),
        Km(rawId = "km"),
        Bio(rawId = "bio"),
        LookingFor(rawId = "looking_for"),

        // Profile screen
        OpenProfileEditor(rawId = "open_profile_editor"),

        // Search Pref screen
        Filters(rawId = "filters"),

        // Profile editor

        // --------------
        ;
        override val id: String = "${rawId}_ui_id"
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

        private val mapUiId = UiId.entries.associateBy(StaticTextId::id)
        private val mapAssetId = AssetId.entries.associateBy(StaticTextId::id)

        fun parse(id: String): StaticTextId? {
            return mapUiId[id] ?: mapAssetId[id]
        }
    }
}
