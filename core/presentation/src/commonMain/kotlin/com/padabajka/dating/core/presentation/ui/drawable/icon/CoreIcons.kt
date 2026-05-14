package com.padabajka.dating.core.presentation.ui.drawable.icon

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.Add
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.AlcoholLifestyle
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.AnimalLifestyle
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.ArrowLeftIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.ArrowRightIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.CameraIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.CancelIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.EditProfileIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.FaqIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.GoogleLogo
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.LanguageIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.LogoutIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MailLogo
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupCopy
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupEdit
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupLike
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupPin
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupReply
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessagePopupTrash
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessageRead
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.MessageSent
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.NavigationLikesIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.NavigationMessageIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.NavigationProfileIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.NavigationSwiperIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.NotificationIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.ReactionLike
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.RewindIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.SearchPrefIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.SettingsIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.SmokingLifestyle
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.Snowman
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.SwiperLikeIcon
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons.SwiperSuperLikeIcon
import com.padabajka.dating.core.presentation.ui.resource.CoreRes
import com.padabajka.dating.core.presentation.ui.resource.drawable.app_logo
import com.padabajka.dating.core.presentation.ui.resource.painterResource

object CoreIcons {

    object NavigationBar {
        val Messenger
            @Composable
            get() = RawIcons.NavigationMessageIcon

        val Likes
            @Composable
            get() = RawIcons.NavigationLikesIcon

        val Swiper
            @Composable
            get() = RawIcons.NavigationSwiperIcon

        val Profile
            @Composable
            get() = RawIcons.NavigationProfileIcon
    }

    val SearchPref
        @Composable
        get() = RawIcons.SearchPrefIcon

    val BackArrow
        @Composable
        get() = RawIcons.ArrowLeftIcon

    val RightArrow
        @Composable
        get() = RawIcons.ArrowRightIcon

    val Add
        @Composable
        get() = RawIcons.Add

    object Editor {
        val Camera
            @Composable
            get() = RawIcons.CameraIcon

        val EditProfile
            @Composable
            get() = RawIcons.EditProfileIcon
    }

    object Reaction {
        val Like
            @Composable
            get() = RawIcons.ReactionLike
    }

    object Message {
        object Popup {
            val Like
                @Composable
                get() = RawIcons.MessagePopupLike
            val Reply
                @Composable
                get() = RawIcons.MessagePopupReply
            val Copy
                @Composable
                get() = RawIcons.MessagePopupCopy
            val Pin
                @Composable
                get() = RawIcons.MessagePopupPin
            val Edit
                @Composable
                get() = RawIcons.MessagePopupEdit
            val Trash
                @Composable
                get() = RawIcons.MessagePopupTrash
        }

        val Sent
            @Composable
            get() = RawIcons.MessageSent

        val Read
            @Composable
            get() = RawIcons.MessageRead
    }

    object Login {
        val MailLogo
            @Composable
            get() = RawIcons.MailLogo

        val GoogleLogo
            @Composable
            get() = RawIcons.GoogleLogo
    }

    object Lifestyle {
        val Smoking
            @Composable
            get() = RawIcons.SmokingLifestyle
        val Alcohol
            @Composable
            get() = RawIcons.AlcoholLifestyle
        val Animals
            @Composable
            get() = RawIcons.AnimalLifestyle
    }

    object Settings {
        val SettingsIcon
            @Composable
            get() = RawIcons.SettingsIcon

        val FAQ
            @Composable
            get() = RawIcons.FaqIcon

        val Language
            @Composable
            get() = RawIcons.LanguageIcon

        val Logout
            @Composable
            get() = RawIcons.LogoutIcon

        val Notification
            @Composable
            get() = RawIcons.NotificationIcon

        val Snowman
            @Composable
            get() = RawIcons.Snowman
    }

    object Swiper {
        val Rewind
            @Composable
            get() = RawIcons.RewindIcon

        val Dislike
            @Composable
            get() = RawIcons.CancelIcon

        val Like
            @Composable
            get() = RawIcons.SwiperLikeIcon

        val SuperLike
            @Composable
            get() = RawIcons.SwiperSuperLikeIcon
    }

    val AppLogo
        @Composable
        get() = painterResource(CoreRes.img.app_logo)
}
