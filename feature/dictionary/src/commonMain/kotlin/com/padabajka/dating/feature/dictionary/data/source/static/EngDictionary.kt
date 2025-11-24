package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

@Suppress("CyclomaticComplexMethod")
class EngDictionary : Dictionary() {
    @Suppress("LongMethod")
    override fun getUiWord(uiId: StaticTextId.UiId): String {
        return when (uiId) {
            StaticTextId.UiId.Settings -> "Settings"
            StaticTextId.UiId.Profile -> "Profile"
            StaticTextId.UiId.Editor -> "Editor"
            StaticTextId.UiId.Matches -> "Matches"
            StaticTextId.UiId.Chats -> "Chats"
            StaticTextId.UiId.Likes -> "Likes"
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
            StaticTextId.UiId.CityHint -> "Which city are you from?"
            StaticTextId.UiId.SexualOrientation -> "Sexual Orientation"
            StaticTextId.UiId.SexualOrientationHint -> "Who are you into?"
            StaticTextId.UiId.Education -> "Education"
            StaticTextId.UiId.EducationHint -> "Where did you study?"
            StaticTextId.UiId.Profession -> "Profession"
            StaticTextId.UiId.ProfessionHint -> "What do you do for work?"
            StaticTextId.UiId.ProfessionSelectorTitle -> "Your profession"
            StaticTextId.UiId.ProfessionSelectorBody -> "Tell us what you do"
            StaticTextId.UiId.ProfessionSelectorHint -> "For example: designer, doctor, programmer..."
            StaticTextId.UiId.Height -> "Height"
            StaticTextId.UiId.HeightHint -> "How tall are you?"
            StaticTextId.UiId.Character -> "Character"
            StaticTextId.UiId.CharacterHint -> "Are you more of an introvert or an extrovert?"
            StaticTextId.UiId.Language -> "Language"
            StaticTextId.UiId.LanguageHint -> "Which languages do you speak?"
            StaticTextId.UiId.CitySearch -> "City search"
            StaticTextId.UiId.Apply -> "Apply"
            StaticTextId.UiId.Reset -> "Reset"
            StaticTextId.UiId.Send -> "Send"
            StaticTextId.UiId.WelcomeText ->
                "Welcome to Padabajka! " +
                    "We’re glad to have you here. Let’s get to know each other.\n\n" +
                    "You can create your profile now to tell us more about yourself, " +
                    "or skip this step and start using the app right away."

            StaticTextId.UiId.CreateProfileBtn -> "Create profile"
            StaticTextId.UiId.ContinueBtn -> "Continue"
            StaticTextId.UiId.HowNameTitle -> "What’s your name?"
            StaticTextId.UiId.HowNameBody -> "It’s best to use your real one"
            StaticTextId.UiId.HintForName -> "Name"
            StaticTextId.UiId.WhenBirthdayTitle -> "When’s your birthday?"
            StaticTextId.UiId.WhenBirthdayBody -> "Only your age is visible on your profile"
            StaticTextId.UiId.HintForBirthday -> "Your birthday"
            StaticTextId.UiId.MessageForTooYoung -> "Sorry, registration is only available for users 18 and older"
            StaticTextId.UiId.MessageForTooOld ->
                "Please check your date of birth — it looks like the age might be incorrect"

            StaticTextId.UiId.WhatBiologicalSexTitle -> "What’s your gender?"
            StaticTextId.UiId.WhatBiologicalSexBody -> "You won’t be able to change this later"
            StaticTextId.UiId.WhatPreferenceTitle -> "Who would you like to meet?"
            StaticTextId.UiId.WhatPreferenceBody -> "You can change this anytime"
            StaticTextId.UiId.WhatLookingForTitle -> "What are you looking for?"
            StaticTextId.UiId.WhatLookingForBody -> "You can change this anytime"
            StaticTextId.UiId.WhatAvatarTitle -> "What do you look like?"
            StaticTextId.UiId.WhatAvatarBody -> "You can change the photo at any time"
            StaticTextId.UiId.CreateProfileText -> "We’re setting up your profile, please wait..."
            StaticTextId.UiId.GeoPermissionScreenTitle -> "Share your location to find people nearby"
            StaticTextId.UiId.NotificationPermissionScreenTitle ->
                "Enable notifications so you don’t miss important messages"

            StaticTextId.UiId.FinishScreenTitle ->
                "All required permissions are already enabled \uD83C\uDF89\n\n" +
                    "You can now use the app to its full potential"

            StaticTextId.UiId.SuperLikeTitle -> "Super like"
            StaticTextId.UiId.SuperLikeBody -> "Write an interesting message, {name} will see it!"
            StaticTextId.UiId.HintSuperLikeMessage -> "How about starting with a compliment?"
            StaticTextId.UiId.MessageTitle -> "message:"
            StaticTextId.UiId.SuperLikeCountTitle -> "you have available:"
            StaticTextId.UiId.EmptyReactionsScreen -> "No reactions yet, be the first!"
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

            StaticTextId.AssetId.Heterosexual -> "Heterosexual"
            StaticTextId.AssetId.Homosexual -> "Homosexual"
            StaticTextId.AssetId.Bisexual -> "Bisexual"

            StaticTextId.AssetId.Extrovert -> "Extrovert"
            StaticTextId.AssetId.Introvert -> "Introvert"
            StaticTextId.AssetId.Ambidextrous -> "Ambidextrous"
        }
    }
}
