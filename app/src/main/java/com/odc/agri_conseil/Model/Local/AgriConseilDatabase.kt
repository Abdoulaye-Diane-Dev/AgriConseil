package com.odc.agri_conseil.Model.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.odc.agri_conseil.Model.Local.DAO.ActiviteDao
import com.odc.agri_conseil.Model.Local.DAO.CultureDao
import com.odc.agri_conseil.Model.Local.DAO.ParcelleDao
import com.odc.agri_conseil.Model.Local.DAO.PlantationDao
import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.Local.Entity.Plantation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Culture::class, Parcelle::class, Plantation::class, Activite::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AgriConseilDatabase : RoomDatabase() {

    abstract fun cultureDao(): CultureDao
    abstract fun parcelleDao(): ParcelleDao
    abstract fun plantationDao(): PlantationDao
    abstract fun activiteDao(): ActiviteDao

    companion object {
        @Volatile
        private var INSTANCE: AgriConseilDatabase? = null

        private fun seedCultures(db: SupportSQLiteDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                CultureSeed.data.forEach { culture ->
                    db.execSQL(
                        "INSERT INTO cultures (nom, categorie, moisSemis, dureeCycleJours, conseils) VALUES (?, ?, ?, ?, ?)",
                        arrayOf(culture.nom, culture.categorie, culture.moisSemis, culture.dureeCycleJours, culture.conseils)
                    )
                }
            }
        }

        fun getInstance(context: Context): AgriConseilDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AgriConseilDatabase::class.java,
                    "agri_conseil.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            seedCultures(db)
                        }

                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
                            seedCultures(db)
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}