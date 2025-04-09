package com.padabajka.dating.core.presentation.ui.dictionary

sealed interface StaticTextId {
    val id: String

    enum class UiId(override val id: String) : StaticTextId {
        // Headers
        Settings(id = "settings"),
        Profile(id = "profile"),
        Editor(id = "editor"),

        // Setting screen
        Name(id = "name"),
        LogOut(id = "log_out"),
        General(id = "general_error"),

        // Total for Profile
        Male(id = "male"),
        Female(id = "female"),
        Everyone(id = "everyone"),
        Age(id = "age"),
        Distance(id = "distance"),
        Km(id = "km"),
        Bio(id = "bio"),

        // Profile screen
        OpenProfileEditor(id = "open_profile_editor"),

        // Search Pref screen
        Filters(id = "filters")
    }

    enum class AssetId(override val id: String) : StaticTextId {
        // locking_for
        JustMeeting(id = "just_meeting"),
        LookingForFriends(id = "looking_for_friends"),
        PeopleToGoOutWith(id = "people_to_go_out_with"),
        LanguageExchange(id = "language_exchange"),
        Networking(id = "networking"),

        CasualDating(id = "casual_dating"),
        Dating(id = "dating"),
        Flirt(id = "flirt"),
        JustForFun(id = "just_for_fun")
    }

    companion object {

        private val mapUiId = UiId.entries.associateBy(StaticTextId::id)
        private val mapAssetId = AssetId.entries.associateBy(StaticTextId::id)

        fun parse(id: String): StaticTextId? {
            return mapUiId[id] ?: mapAssetId[id]
        }
    }
}
