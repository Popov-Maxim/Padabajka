package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

@Suppress("CyclomaticComplexMethod")
class EngDictionary : Dictionary() {
    override fun getUiWord(uiId: StaticTextId.UiId): String {
        return when (uiId) {
            StaticTextId.UiId.Settings -> "Settings"
            StaticTextId.UiId.Profile -> "Profile"
            StaticTextId.UiId.Editor -> "Editor"
            StaticTextId.UiId.Matches -> "Matches"
            StaticTextId.UiId.Chats -> "Chats"
            StaticTextId.UiId.Name -> "Name"
            StaticTextId.UiId.LogOut -> "Logout"
            StaticTextId.UiId.General -> "General"
            StaticTextId.UiId.Male -> "Male"
            StaticTextId.UiId.Female -> "Female"
            StaticTextId.UiId.Everyone -> "Everyone"
            StaticTextId.UiId.Age -> "Age"
            StaticTextId.UiId.Distance -> "Distance"
            StaticTextId.UiId.Km -> "km"
            StaticTextId.UiId.Bio -> "Bio"
            StaticTextId.UiId.OpenProfileEditor -> "Open profile editor"
            StaticTextId.UiId.Preview -> "preview"
            StaticTextId.UiId.Filters -> "Filters"
            StaticTextId.UiId.LookingFor -> "Looking for..."
            StaticTextId.UiId.BasicInfo -> "Basic info"
            StaticTextId.UiId.EnterMessage -> "Enter message..."
            StaticTextId.UiId.MessagePopupLike -> "Like"
            StaticTextId.UiId.MessagePopupReply -> "Reply"
            StaticTextId.UiId.MessagePopupCopy -> "Copy"
            StaticTextId.UiId.MessagePopupPin -> "Pin"
            StaticTextId.UiId.MessagePopupEdit -> "Edit"
            StaticTextId.UiId.MessagePopupDelete -> "Delete"
            StaticTextId.UiId.City -> "City"
            StaticTextId.UiId.SexualOrientation -> "Sexual Orientation"
            StaticTextId.UiId.Education -> "Education"
            StaticTextId.UiId.Profession -> "Profession"
            StaticTextId.UiId.Height -> "Height"
            StaticTextId.UiId.Character -> "Character"
            StaticTextId.UiId.Language -> "Language"
            StaticTextId.UiId.CitySearch -> "City search"
        }
    }

    override fun getAssetWord(assetId: StaticTextId.AssetId): String {
        return when (assetId) {
            StaticTextId.AssetId.NonRomantic -> "Non-romantic"
            StaticTextId.AssetId.LookingForFriends -> "Looking for friends"
            StaticTextId.AssetId.PeopleToGoOutWith -> "People to go out with"
            StaticTextId.AssetId.LanguageExchange -> "Language exchange"
            StaticTextId.AssetId.Networking -> "Networking"

            StaticTextId.AssetId.CasualRelationship -> "Casual relationship"
            StaticTextId.AssetId.Dating -> "Dating"
            StaticTextId.AssetId.Flirt -> "Flirt"
            StaticTextId.AssetId.JustForFun -> "Just for fun"

            StaticTextId.AssetId.SeriousRelationship -> "Serious relationship"
            StaticTextId.AssetId.BuildingFamily -> "Building family"
        }
    }
}
