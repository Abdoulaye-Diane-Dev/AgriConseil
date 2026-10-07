package com.odc.agri_conseil.Notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.odc.agri_conseil.AgriApplication
import java.util.concurrent.TimeUnit

/**
 * Tourne une fois par jour (planifié depuis AgriApplication) :
 * regarde les activités non faites prévues pour aujourd'hui et pour
 * les 7 prochains jours, puis déclenche les notifications correspondantes.
 */
class RappelsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val repository = (applicationContext as AgriApplication).repository

        val unJour = TimeUnit.DAYS.toMillis(1)
        val maintenant = System.currentTimeMillis()

        val activitesDuJour = repository.getActivitesEntreDates(maintenant, maintenant + unJour)
        val activitesDeLaSemaine = repository.getActivitesEntreDates(maintenant, maintenant + 7 * unJour)

        NotificationHelper.creerCanal(applicationContext)
        NotificationHelper.notifierActivitesDuJour(applicationContext, activitesDuJour)
        NotificationHelper.notifierResumeSemaine(applicationContext, activitesDeLaSemaine.size)

        return Result.success()
    }
}
