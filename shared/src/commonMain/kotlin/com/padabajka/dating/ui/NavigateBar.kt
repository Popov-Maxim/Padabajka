package com.padabajka.dating.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun NavigateBar(
    openSwiper: () -> Unit,
    openProfile: () -> Unit
) {
    Row(
        modifier = Modifier.height(65.dp)
//        .background(CoreColors.primary.mainColor).border(1.dp, Color.Black)
    ) {
        val iconData = persistentListOf(
            CoreIcons.NavigationBar.Messenger.toIconData("Messenger") {},
            CoreIcons.NavigationBar.Likes.toIconData("Likes") {},
            CoreIcons.NavigationBar.Swiper.toIconData("Swiper", openSwiper),
            CoreIcons.NavigationBar.Profile.toIconData("Profile", openProfile)
        ).toImmutableList()
        iconData.onEach {
            NavigationIcon(
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                onClick = it.onClick
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = it.painter,
                    contentDescription = it.contentDescription
                )
            }
        }
    }
}

private data class IconData(
    val painter: Painter,
    val contentDescription: String?,
    val onClick: () -> Unit
)

private fun Painter.toIconData(contentDescription: String?, onClick: () -> Unit) = IconData(
    painter = this,
    contentDescription = contentDescription,
    onClick = onClick
)

@Composable
private fun NavigationIcon(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    IconButton(onClick, modifier) {
        icon()
    }
}
