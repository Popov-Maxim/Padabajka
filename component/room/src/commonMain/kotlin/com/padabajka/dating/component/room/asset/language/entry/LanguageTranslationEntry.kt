package com.padabajka.dating.component.room.asset.language.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.padabajka.dating.core.repository.api.model.profile.Text

@Entity(tableName = "language_translations", primaryKeys = ["id", "language"])
data class LanguageTranslationEntry(
    val id: String,
    val language: String,
    @ColumnInfo("name", collate = ColumnInfo.NOCASE) val name: String
)

fun LanguageTranslationEntry.toDomain(): Text {
    return Text(
        id = id.let { Text.Id(it) },
        type = Text.Type.Language,
        default = name
    )
}
