package com.padabajka.dating.component.room.person.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.padabajka.dating.core.data.network.model.DetailDto
import com.padabajka.dating.core.data.network.model.LanguagesAssetDto
import com.padabajka.dating.core.data.network.model.LifestyleDto
import com.padabajka.dating.core.data.network.model.LookingForDataDto
import com.padabajka.dating.core.data.network.model.TextDto
import kotlinx.datetime.LocalDate

@Entity(tableName = "person")
class PersonEntry(
    @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("birthday") val birthday: LocalDate,
    @ColumnInfo("images") val images: List<String>,
    @ColumnInfo("aboutMe") val aboutMe: String,
    @ColumnInfo("lookingFor") val lookingFor: LookingForDataDto,
    @ColumnInfo("details") val details: List<DetailDto>,
    @ColumnInfo("lifestyles") val lifestyles: List<LifestyleDto>,
    @ColumnInfo("interests") val interests: List<TextDto>,
    @ColumnInfo("languagesAsset") val languagesAsset: LanguagesAssetDto,
)
