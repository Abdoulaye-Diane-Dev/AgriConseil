package com.odc.agri_conseil

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.odc.agri_conseil.Model.Local.AgriConseilDatabase
import com.odc.agri_conseil.Model.repository.AgriRepository
import com.odc.agri_conseil.Model.repository.AgriRepositoryImpl
import com.odc.agri_conseil.Notifications.RappelsWorker
import java.util.concurrent.TimeUnit

class AgriApplication : Application() {
    val repository: AgriRepository by lazy {
        AgriRepositoryImpl(AgriConseilDatabase.getInstance(this))
    }

    override fun onCreate() {
        super.onCreate()
        planifierRappelsQuotidiens()
    }

    private fun planifierRappelsQuotidiens() {

        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<RappelsWorker>().build())


        val demande = PeriodicWorkRequestBuilder<RappelsWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "rappels_quotidiens",
            ExistingPeriodicWorkPolicy.KEEP,
            demande
        )
    }
}