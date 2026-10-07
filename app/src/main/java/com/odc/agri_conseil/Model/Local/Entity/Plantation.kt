package com.odc.agri_conseil.Model.Local.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plantations",
    foreignKeys = [
        ForeignKey(
            entity = Parcelle::class,
            parentColumns = ["id"],
            childColumns = ["parcelleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Culture::class,
            parentColumns = ["id"],
            childColumns = ["cultureId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parcelleId"), Index("cultureId")]
)
data class Plantation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parcelleId: Long,
    val cultureId: Long,
    val dateSemis: Long,
    val statut: StatutPlantation = StatutPlantation.EN_COURS
)