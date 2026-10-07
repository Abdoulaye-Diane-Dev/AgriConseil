package com.odc.agri_conseil.Model.Local.Entity

data class PlantationActiveAvecCulture(
    val id: Long,
    val parcelleId: Long,
    val cultureNom: String,
    val dateSemis: Long
)