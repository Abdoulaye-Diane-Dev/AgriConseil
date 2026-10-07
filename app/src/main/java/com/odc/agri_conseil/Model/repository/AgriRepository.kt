package com.odc.agri_conseil.Model.repository

import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.ActiviteAvecDetails
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.Local.Entity.Plantation
import com.odc.agri_conseil.Model.Local.Entity.PlantationActiveAvecCulture
import kotlinx.coroutines.flow.Flow

interface AgriRepository {
    fun getCultures(): Flow<List<Culture>>
    fun getParcelles(): Flow<List<Parcelle>>
    suspend fun creerParcelle(parcelle: Parcelle): Long
    suspend fun supprimerParcelle(parcelle: Parcelle)
    fun getPlantations(): Flow<List<Plantation>>

    fun getPlantationsActivesAvecCulture(): Flow<List<PlantationActiveAvecCulture>>
    suspend fun creerPlantation(parcelleId: Long, cultureId: Long, dateSemis: Long): Long

    fun getActivitesAVenir(maintenant: Long): Flow<List<ActiviteAvecDetails>>
    fun getActivitesEnRetard(maintenant: Long): Flow<List<ActiviteAvecDetails>>
    fun getActivitesFaitesParPlantation(plantationId: Long): Flow<List<Activite>>
    suspend fun getActivitesEntreDates(debut: Long, fin: Long): List<ActiviteAvecDetails>
    suspend fun marquerActiviteFaite(activiteId: Long)
}

