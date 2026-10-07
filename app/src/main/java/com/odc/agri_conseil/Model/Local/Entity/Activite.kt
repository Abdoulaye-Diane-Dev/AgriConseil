package com.odc.agri_conseil.Model.Local.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activites",
    foreignKeys = [
        ForeignKey(
            entity = Plantation::class,
            parentColumns = ["id"],
            childColumns = ["plantationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("plantationId")]
)
data class Activite(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantationId: Long,
    val type: TypeActivite,
    val datePrevue: Long,
    val faite: Boolean = false,
)