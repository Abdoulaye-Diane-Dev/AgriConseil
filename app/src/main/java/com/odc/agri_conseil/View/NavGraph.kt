package com.orangedigitalcenter.agriconseil.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.odc.agri_conseil.View.LeafIcon
import com.odc.agri_conseil.View.ParcelleIcon
import com.odc.agri_conseil.View.SplashScreen
import com.odc.agri_conseil.View.SproutIcon
import com.odc.agri_conseil.ViewModel.AgriViewModelFactory
import com.odc.agri_conseil.ViewModel.CatalogueCulturesViewModel
import com.odc.agri_conseil.ViewModel.ParcellesViewModel
import com.odc.agri_conseil.ViewModel.PlantationFormViewModel
import com.odc.agri_conseil.ViewModel.TableauBordViewModel
import com.orangedigitalcenter.agriconseil.ui.CatalogueCulturesScreen
import com.orangedigitalcenter.agriconseil.ui.ParcellesScreen
import com.orangedigitalcenter.agriconseil.ui.PlantationFormScreen
import com.orangedigitalcenter.agriconseil.ui.TableauBordScreen
import androidx.compose.foundation.layout.WindowInsets

sealed class Screen(val route: String, val label: String) {
    object Dashboard : Screen("dashboard", "Acceuil")
    object Catalogue : Screen("catalogue", "Cultures")
    object Parcelles : Screen("parcelles", "Parcelles")
    object Plantation : Screen("plantation", "Planter")
}

private val items = listOf(Screen.Dashboard, Screen.Catalogue, Screen.Parcelles, Screen.Plantation)

@Composable
fun AgriNavGraph(factory: AgriViewModelFactory) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
        return
    }

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { NavIcon(screen) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                val vm: TableauBordViewModel = viewModel(factory = factory)
                TableauBordScreen(vm)
            }
            composable(Screen.Catalogue.route) {
                val vm: CatalogueCulturesViewModel = viewModel(factory = factory)
                CatalogueCulturesScreen(vm)
            }
            composable(Screen.Parcelles.route) {
                val vm: ParcellesViewModel = viewModel(factory = factory)
                ParcellesScreen(vm)
            }
            composable(Screen.Plantation.route) {
                val vm: PlantationFormViewModel = viewModel(factory = factory)
                PlantationFormScreen(vm)
            }
        }
    }
}

@Composable
private fun NavIcon(screen: Screen) {
    when (screen) {
        Screen.Dashboard -> Icon(Icons.Default.Home, contentDescription = screen.label)
        Screen.Catalogue -> LeafIcon()
        Screen.Parcelles -> ParcelleIcon()
        Screen.Plantation -> SproutIcon()
    }
}
