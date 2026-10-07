package com.odc.agri_conseil.Model.repository

import androidx.room.withTransaction
import com.odc.agri_conseil.Model.Local.AgriConseilDatabase
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.Local.Entity.Plantation
import com.odc.agri_conseil.Model.Local.Entity.StatutPlantation
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite

class AgriRepositoryImpl(
    private val database: AgriConseilDatabase
) : AgriRepository {

    private val cultureDao = database.cultureDao()
    private val parcelleDao = database.parcelleDao()
    private val plantationDao = database.plantationDao()
    private val activiteDao = database.activiteDao()

    override fun getCultures() = cultureDao.getAll()
    override fun getParcelles() = parcelleDao.getAll()
    override suspend fun creerParcelle(parcelle: Parcelle) = parcelleDao.insert(parcelle)
    override suspend fun supprimerParcelle(parcelle: Parcelle) = parcelleDao.delete(parcelle)
    override fun getPlantations() = plantationDao.getAll()

    override fun getPlantationsActivesAvecCulture() = plantationDao.getActivesAvecCulture()

    override suspend fun creerPlantation(parcelleId: Long, cultureId: Long, dateSemis: Long): Long {
        val culture = cultureDao.getById(cultureId)!!
        val plantation = Plantation(parcelleId = parcelleId, cultureId = cultureId, dateSemis = dateSemis)
        val plantationId = plantationDao.insert(plantation)
        val calendrier = CalendrierGenerator.genererCalendrier(plantationId, dateSemis, culture)
        activiteDao.insertAll(calendrier)
        return plantationId
    }

    override fun getActivitesAVenir(maintenant: Long) = activiteDao.getAVenir(maintenant)
    override fun getActivitesEnRetard(maintenant: Long) = activiteDao.getEnRetard(maintenant)
    override fun getActivitesFaitesParPlantation(plantationId: Long) = activiteDao.getFaitesParPlantation(plantationId)
    override suspend fun getActivitesEntreDates(debut: Long, fin: Long) = activiteDao.getEntreDates(debut, fin)
    override suspend fun marquerActiviteFaite(activiteId: Long) {
        database.withTransaction {
            activiteDao.marquerFaite(activiteId)
            val activite = activiteDao.getById(activiteId) ?: return@withTransaction
            if (activite.type == TypeActivite.RECOLTE) {
                val plantation = plantationDao.getById(activite.plantationId) ?: return@withTransaction
                plantationDao.update(plantation.copy(statut = StatutPlantation.RECOLTE))
            }
        }
    }
}
