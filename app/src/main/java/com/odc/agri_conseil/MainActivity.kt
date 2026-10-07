package com.odc.agri_conseil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.odc.agri_conseil.ViewModel.AgriViewModelFactory
import com.odc.agri_conseil.ui.theme.AgriConseilTheme
import com.orangedigitalcenter.agriconseil.ui.navigation.AgriNavGraph

class MainActivity : ComponentActivity() {

    private val demandePermissionNotification =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* rappels simplement désactivés si refusé */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        demanderPermissionNotificationSiNecessaire()

        val repository = (application as AgriApplication).repository
        val factory = AgriViewModelFactory(repository)

        setContent {
            AgriConseilTheme {
                AgriNavGraph(factory)
            }
        }
    }

    private fun demanderPermissionNotificationSiNecessaire() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val dejaAccordee = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!dejaAccordee) {
                demandePermissionNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

