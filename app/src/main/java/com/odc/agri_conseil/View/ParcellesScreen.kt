package com.orangedigitalcenter.agriconseil.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.Local.Entity.PlantationActiveAvecCulture
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite
import com.odc.agri_conseil.View.AppHeaderSecond
import com.odc.agri_conseil.View.ScrollFadeOverlay
import com.odc.agri_conseil.ViewModel.ParcellesViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ParcellesScreen(viewModel: ParcellesViewModel) {
    val parcelles by viewModel.parcelles.collectAsState()
    val plantationsParParcelle by viewModel.plantationsParParcelle.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var parcelleASupprimer by remember { mutableStateOf<Parcelle?>(null) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = { AppHeaderSecond(title = "Mes parcelles") },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter une parcelle")
            }
        }
    ) { padding ->
        if (parcelles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucune donnée pour le moment", fontSize = 14.sp)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(parcelles, key = { it.id }) { parcelle ->
                        ParcelleCard(
                            parcelle = parcelle,
                            plantations = plantationsParParcelle[parcelle.id] ?: emptyList(),
                            viewModel = viewModel,
                            onDelete = { parcelleASupprimer = parcelle }
                        )
                    }
                }
                ScrollFadeOverlay(listState, Modifier.align(Alignment.TopCenter))
            }
        }
    }

    if (showDialog) {
        AjouterParcelleDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nom, superficie, localisation ->
                viewModel.ajouterParcelle(nom, superficie, localisation)
                showDialog = false
            }
        )
    }

    parcelleASupprimer?.let { parcelle ->
        val nombrePlantations = (plantationsParParcelle[parcelle.id] ?: emptyList()).size
        AlertDialog(
            onDismissRequest = { parcelleASupprimer = null },
            title = { Text("Supprimer \"${parcelle.nom}\" ?") },
            text = {
                Text(
                    if (nombrePlantations > 0)
                        "Cette parcelle a $nombrePlantations plantation(s) active(s). Elles seront supprimées avec leurs activités. Cette action est irréversible."
                    else
                        "Cette action est irréversible.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.supprimerParcelle(parcelle)
                    parcelleASupprimer = null
                }) { Text("Supprimer") }
            },
            dismissButton = {
                TextButton(onClick = { parcelleASupprimer = null }) { Text("Annuler") }
            }
        )
    }
}

@Composable
private fun ParcelleCard(
    parcelle: Parcelle,
    plantations: List<PlantationActiveAvecCulture>,
    viewModel: ParcellesViewModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(parcelle.nom, fontSize = 18.sp)
                    Text("${parcelle.superficieHa} ha — ${parcelle.localisation}", fontSize = 14.sp)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (plantations.isEmpty()) {
                Text(
                    "Aucune plantation active",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                plantations.forEach { plantation ->
                    PlantationLigne(plantation = plantation, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun PlantationLigne(
    plantation: PlantationActiveAvecCulture,
    viewModel: ParcellesViewModel
) {
    var ouvert by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { ouvert = !ouvert }
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${plantation.cultureNom} — semé le ${formatDate(plantation.dateSemis)}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Historique",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    if (ouvert) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (ouvert) "Réduire l'historique" else "Voir l'historique",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (ouvert) {
            val activitesFaites by remember(plantation.id) {
                viewModel.activitesFaitesPourPlantation(plantation.id)
            }.collectAsState(initial = emptyList())

            Column(modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)) {
                if (activitesFaites.isEmpty()) {
                    Text(
                        "Aucune activité terminée pour cette plantation",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    activitesFaites.forEach { activite ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(libelleActivite(activite.type), fontSize = 13.sp)
                            }
                            Text(
                                formatDate(activite.datePrevue),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun libelleActivite(type: TypeActivite): String = when (type) {
    TypeActivite.DESHERBAGE -> "Désherbage"
    TypeActivite.FERTILISATION -> "Fertilisation"
    TypeActivite.RECOLTE -> "Récolte"
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(millis)

@Composable
private fun AjouterParcelleDialog(
    onDismiss: () -> Unit,
    onConfirm: (nom: String, superficieHa: Double, localisation: String) -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var superficie by remember { mutableStateOf("") }
    var localisation by remember { mutableStateOf("") }
    var erreur by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouvelle parcelle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = superficie,
                    onValueChange = { superficie = it },
                    label = { Text("Superficie (ha)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                OutlinedTextField(
                    value = localisation,
                    onValueChange = { localisation = it },
                    label = { Text("Localisation") },
                    singleLine = true
                )
                erreur?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val superficieValeur = superficie.toDoubleOrNull()
                when {
                    nom.isBlank() -> erreur = "Le nom est obligatoire."
                    superficieValeur == null || superficieValeur <= 0 -> erreur = "Superficie invalide."
                    localisation.isBlank() -> erreur = "La localisation est obligatoire."
                    else -> onConfirm(nom, superficieValeur, localisation)
                }
            }) { Text("Ajouter") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}


