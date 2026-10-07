package com.odc.agri_conseil.Model.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import kotlinx.coroutines.flow.Flow

@Dao
interface ParcelleDao {
    @Insert
    suspend fun insert(parcelle: Parcelle): Long

    @Update
    suspend fun update(parcelle: Parcelle)

    @Delete
    suspend fun delete(parcelle: Parcelle)

    @Query("SELECT * FROM parcelles ORDER BY nom ASC")
    fun getAll(): Flow<List<Parcelle>>

    @Query("SELECT * FROM parcelles WHERE id = :id")
    suspend fun getById(id: Long): Parcelle?
}