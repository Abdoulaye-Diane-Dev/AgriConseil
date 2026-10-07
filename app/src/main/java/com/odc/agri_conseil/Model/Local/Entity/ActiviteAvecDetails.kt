package com.odc.agri_conseil.Model.Local.Entity

data class ActiviteAvecDetails(
    val id: Long,
    val type: TypeActivite,
    val datePrevue: Long,
    val faite: Boolean,
    val cultureNom: String,
    val parcelleNom: String
)