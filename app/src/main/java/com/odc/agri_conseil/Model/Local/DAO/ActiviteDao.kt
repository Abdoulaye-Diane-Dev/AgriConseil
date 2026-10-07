package com.odc.agri_conseil.Model.Local.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.ActiviteAvecDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiviteDao {
    @Insert
    suspend fun insertAll(activites: List<Activite>)

    @Update
    suspend fun update(activite: Activite)

    @Query("SELECT * FROM activites WHERE plantationId = :plantationId ORDER BY datePrevue ASC")
    fun getByPlantation(plantationId: Long): Flow<List<Activite>>

    @Query("""
        SELECT a.id, a.type, a.datePrevue, a.faite, c.nom AS cultureNom, p.nom AS parcelleNom
        FROM activites a
        JOIN plantations pl ON pl.id = a.plantationId
        JOIN cultures c ON c.id = pl.cultureId
        JOIN parcelles p ON p.id = pl.parcelleId
        WHERE a.faite = 0 AND a.datePrevue >= :maintenant AND pl.statut = 'EN_COURS'
        ORDER BY a.datePrevue ASC
    """)
    fun getAVenir(maintenant: Long): Flow<List<ActiviteAvecDetails>>

    @Query("""
        SELECT a.id, a.type, a.datePrevue, a.faite, c.nom AS cultureNom, p.nom AS parcelleNom
        FROM activites a
        JOIN plantations pl ON pl.id = a.plantationId
        JOIN cultures c ON c.id = pl.cultureId
        JOIN parcelles p ON p.id = pl.parcelleId
        WHERE a.faite = 0 AND a.datePrevue < :maintenant AND pl.statut = 'EN_COURS'
        ORDER BY a.datePrevue ASC
    """)
    fun getEnRetard(maintenant: Long): Flow<List<ActiviteAvecDetails>>

    @Query("SELECT * FROM activites WHERE id = :id")
    suspend fun getById(id: Long): Activite?

    // Historique des activités terminées d'UNE plantation précise — utilisé par
    // l'écran "Mes parcelles" pour afficher l'historique au bon endroit, par plantation.
    @Query("SELECT * FROM activites WHERE plantationId = :plantationId AND faite = 1 ORDER BY datePrevue DESC")
    fun getFaitesParPlantation(plantationId: Long): Flow<List<Activite>>

    @Query("UPDATE activites SET faite = 1 WHERE id = :id")
    suspend fun marquerFaite(id: Long)

    @Query("""
        SELECT a.id, a.type, a.datePrevue, a.faite, c.nom AS cultureNom, p.nom AS parcelleNom
        FROM activites a
        JOIN plantations pl ON pl.id = a.plantationId
        JOIN cultures c ON c.id = pl.cultureId
        JOIN parcelles p ON p.id = pl.parcelleId
        WHERE a.faite = 0 AND a.datePrevue BETWEEN :debut AND :fin AND pl.statut = 'EN_COURS'
        ORDER BY a.datePrevue ASC
    """)
    suspend fun getEntreDates(debut: Long, fin: Long): List<ActiviteAvecDetails>
}
