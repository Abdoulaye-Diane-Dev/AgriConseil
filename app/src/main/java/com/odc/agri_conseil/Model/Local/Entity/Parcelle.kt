package com.odc.agri_conseil.Model.Local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parcelles")
data class Parcelle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val superficieHa: Double,
    val localisation: String
)