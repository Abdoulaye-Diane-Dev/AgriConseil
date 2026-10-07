package com.odc.agri_conseil.Model.Local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cultures")
data class Culture(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val categorie: String,
    val moisSemis: String,
    val dureeCycleJours: Int,
    val conseils: String
)