package com.odc.agri_conseil.Model.Local

import androidx.room.TypeConverter
import com.odc.agri_conseil.Model.Local.Entity.StatutPlantation
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite

class Converters {
    @TypeConverter
    fun fromStatutPlantation(statut: StatutPlantation) : String = statut.name

    @TypeConverter
    fun toStatutPlantation(value: String) : StatutPlantation = StatutPlantation.valueOf(value)

    @TypeConverter
    fun fromTypeActivite(type: TypeActivite) : String = type.name

    @TypeConverter
    fun toTypeActivite(value: String) : TypeActivite = TypeActivite.valueOf(value)
}