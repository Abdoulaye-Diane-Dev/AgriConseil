package com.odc.agri_conseil.Model.Local.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.odc.agri_conseil.Model.Local.Entity.Plantation
import com.odc.agri_conseil.Model.Local.Entity.PlantationActiveAvecCulture
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantationDao {
    @Insert
    suspend fun insert(plantation: Plantation): Long

    @Update
    suspend fun update(plantation: Plantation)

    @Query("SELECT * FROM plantations ORDER BY dateSemis DESC")
    fun getAll(): Flow<List<Plantation>>

    @Query("SELECT * FROM plantations WHERE id = :id")
    suspend fun getById(id: Long): Plantation?

    @Query("SELECT * FROM plantations WHERE parcelleId = :parcelleId")
    fun getByParcelle(parcelleId: Long): Flow<List<Plantation>>

    @Query("""
        SELECT p.id, p.parcelleId, c.nom AS cultureNom, p.dateSemis
        FROM plantations p
        JOIN cultures c ON c.id = p.cultureId
        WHERE p.statut = 'EN_COURS'
        ORDER BY p.dateSemis DESC
    """)
    fun getActivesAvecCulture(): Flow<List<PlantationActiveAvecCulture>>
}
