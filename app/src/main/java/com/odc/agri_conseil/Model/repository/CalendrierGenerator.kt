package com.odc.agri_conseil.Model.repository

import java.util.concurrent.TimeUnit
import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite

object CalendrierGenerator {

    private const val JOURS_DESHERBAGE = 15
    private const val JOURS_FERTILISATION = 30

    fun genererCalendrier(plantationId: Long, dateSemis: Long, culture: Culture): List<Activite> {
        val unJour = TimeUnit.DAYS.toMillis(1)

        return listOf(
            Activite(
                plantationId = plantationId,
                type = TypeActivite.DESHERBAGE,
                datePrevue = dateSemis + JOURS_DESHERBAGE * unJour
            ),
            Activite(
                plantationId = plantationId,
                type = TypeActivite.FERTILISATION,
                datePrevue = dateSemis + JOURS_FERTILISATION * unJour
            ),
            Activite(
                plantationId = plantationId,
                type = TypeActivite.RECOLTE,
                datePrevue = dateSemis + culture.dureeCycleJours * unJour
            )
        )
    }
}