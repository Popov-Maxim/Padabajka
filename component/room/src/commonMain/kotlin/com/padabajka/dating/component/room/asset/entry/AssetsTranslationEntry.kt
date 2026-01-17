package com.padabajka.dating.component.room.asset.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.padabajka.dating.core.repository.api.model.profile.Text

@Entity(tableName = "assets_translation", primaryKeys = ["id", "type", "language"])
data class AssetsTranslationEntry(
    val id: String,
    val type: String,
    val language: String,
    @ColumnInfo("name", collate = ColumnInfo.NOCASE) val name: String
)

fun AssetsTranslationEntry.toDomain(): Text {
    return Text(
        id = id.let { Text.Id(it) },
        type = Text.Type.parse(type),
        default = name
    )
}
