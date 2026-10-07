package com.odc.agri_conseil.Notifications

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.odc.agri_conseil.Model.Local.Entity.ActiviteAvecDetails
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite

/**
 * Regroupe la création du canal de notification et l'envoi des deux rappels :
 * - les activités à faire aujourd'hui (une notification détaillée)
 * - un résumé du nombre d'activités prévues dans les 7 prochains jours
 */
object NotificationHelper {

    private const val CHANNEL_ID = "rappels_agri"
    private const val CHANNEL_NAME = "Rappels d'activités"
    private const val ID_NOTIF_JOUR = 1001
    private const val ID_NOTIF_SEMAINE = 1002

    fun creerCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Rappels des activités agricoles à faire aujourd'hui ou cette semaine"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(canal)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notifierActivitesDuJour(context: Context, activites: List<ActiviteAvecDetails>) {
        if (activites.isEmpty() || !peutNotifier(context)) return

        val texte = activites.joinToString("\n") { a ->
            "${libelleType(a.type)} — ${a.cultureNom} (${a.parcelleNom})"
        }
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_popup_reminder)
            .setContentTitle("Activités agricoles aujourd'hui")
            .setContentText("${activites.size} activité(s) à faire")
            .setStyle(NotificationCompat.BigTextStyle().bigText(texte))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(ID_NOTIF_JOUR, notif)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notifierResumeSemaine(context: Context, nombreActivites: Int) {
        if (nombreActivites == 0 || !peutNotifier(context)) return

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_popup_reminder)
            .setContentTitle("Cette semaine")
            .setContentText("$nombreActivites activité(s) prévue(s) dans les 7 prochains jours")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(ID_NOTIF_SEMAINE, notif)
    }

    private fun peutNotifier(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun libelleType(type: TypeActivite): String = when (type) {
        TypeActivite.DESHERBAGE -> "Désherbage"
        TypeActivite.FERTILISATION -> "Fertilisation"
        TypeActivite.RECOLTE -> "Récolte"
    }
}
