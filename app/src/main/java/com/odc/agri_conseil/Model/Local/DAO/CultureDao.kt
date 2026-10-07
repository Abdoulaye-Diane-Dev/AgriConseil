package com.odc.agri_conseil.Model.Local.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.odc.agri_conseil.Model.Local.Entity.Culture
import kotlinx.coroutines.flow.Flow

@Dao
interface CultureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cultures: List<Culture>)

    @Query("SELECT * FROM cultures ORDER BY nom ASC")
    fun getAll(): Flow<List<Culture>>

    @Query("SELECT * FROM cultures WHERE id = :id")
    suspend fun getById(id: Long): Culture?

    @Query("SELECT COUNT (*) FROM cultures")
    suspend fun count(): Int
}