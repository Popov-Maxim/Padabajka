package com.fp.padabajka.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
            Icons.Default.Menu.toIconData("Messenger") {},
            Icons.Default.Favorite.toIconData("Likes") {},
            Icons.Default.Search.toIconData("Swiper", openSwiper),
            Icons.Default.Person.toIconData("Profile", openProfile)
        ).toImmutableList()
        iconData.onEach {
            NavigationIcon(
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                onClick = it.onClick
            ) {
                Icon(
                    imageVector = it.imageVector,
                    contentDescription = it.contentDescription,
                )
            }
        }
    }
}

private data class IconData(
    val imageVector: ImageVector,
    val contentDescription: String?,
    val onClick: () -> Unit
)

private fun ImageVector.toIconData(contentDescription: String?, onClick: () -> Unit) = IconData(
    imageVector = this,
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
