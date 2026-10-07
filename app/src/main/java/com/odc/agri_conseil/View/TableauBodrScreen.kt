package com.orangedigitalcenter.agriconseil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odc.agri_conseil.Model.Local.Entity.ActiviteAvecDetails
import com.odc.agri_conseil.Model.Local.Entity.TypeActivite
import com.odc.agri_conseil.View.AppHeader
import com.odc.agri_conseil.View.ParcelleIcon
import com.odc.agri_conseil.View.SproutIcon
import com.odc.agri_conseil.View.ScrollFadeOverlay
import com.odc.agri_conseil.ViewModel.TableauBordViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TableauBordScreen(viewModel: TableauBordViewModel) {
    val enRetard by viewModel.activitesEnRetard.collectAsState()
    val aVenir by viewModel.activitesAVenir.collectAsState()
    val nombreParcelles by viewModel.nombreParcelles.collectAsState()
    val nombrePlantations by viewModel.nombrePlantations.collectAsState()
    val listState = rememberLazyListState()
    var voirToutRetard by remember { mutableStateOf(false) }
    var voirToutAVenir by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(title = "Bonjour !", subtitle = "Voici un aperçu de votre exploitation.")

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        StatCard(
                            label = "Parcelles",
                            valeur = nombreParcelles,
                            icone = { ParcelleIcon(tint = it) },
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Plantations",
                            valeur = nombrePlantations,
                            icone = { SproutIcon(tint = it) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        StatCard(
                            label = "À venir",
                            valeur = aVenir.size,
                            icone = { Icon(Icons.Default.DateRange, contentDescription = null, tint = it) },
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Retards",
                            valeur = enRetard.size,
                            icone = { Icon(Icons.Default.Warning, contentDescription = null, tint = it) },
                            modifier = Modifier.weight(1f),
                            isAlerte = enRetard.isNotEmpty()
                        )
                    }
                }

                if (enRetard.isEmpty() && aVenir.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                            Text("Aucune donnée pour le moment", fontSize = 14.sp)
                        }
                    }
                } else {
                    if (enRetard.isNotEmpty()) {
                        item { SectionTitle("En retard (${enRetard.size})", isAlerte = true) }
                        val retardAffiches = if (voirToutRetard) enRetard else enRetard.take(LIMITE_AFFICHAGE)
                        items(retardAffiches, key = { it.id }) { activite ->
                            ActiviteCard(activite, enRetard = true, onFaite = { viewModel.marquerFaite(activite.id) })
                        }
                        if (enRetard.size > LIMITE_AFFICHAGE) {
                            item {
                                VoirToutBouton(
                                    voirTout = voirToutRetard,
                                    onClick = { voirToutRetard = !voirToutRetard }
                                )
                            }
                        }
                    }
                    item { SectionTitle("À venir (${aVenir.size})", isAlerte = false) }
                    val aVenirAffiches = if (voirToutAVenir) aVenir else aVenir.take(LIMITE_AFFICHAGE)
                    items(aVenirAffiches, key = { it.id }) { activite ->
                        ActiviteCard(activite, enRetard = false, onFaite = { viewModel.marquerFaite(activite.id) })
                    }
                    if (aVenir.size > LIMITE_AFFICHAGE) {
                        item {
                            VoirToutBouton(
                                voirTout = voirToutAVenir,
                                onClick = { voirToutAVenir = !voirToutAVenir }
                            )
                        }
                    }
                }
            }
            ScrollFadeOverlay(listState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    valeur: Int,
    icone: @Composable (tint: Color) -> Unit,
    modifier: Modifier = Modifier,
    isAlerte: Boolean = false
) {
    val couleurTexte = if (isAlerte) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAlerte) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(couleurTexte.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                icone(couleurTexte)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(valeur.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = couleurTexte)
                Text(label, fontSize = 14.sp, color = couleurTexte, maxLines = 1)
            }
        }
    }
}

private const val LIMITE_AFFICHAGE = 3

@Composable
private fun VoirToutBouton(voirTout: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(if (voirTout) "Réduire" else "Voir tout")
    }
}

@Composable
private fun SectionTitle(texte: String, isAlerte: Boolean) {
    Text(texte, fontSize = 16.sp, color = if (isAlerte) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
}

@Composable
private fun ActiviteCard(activite: ActiviteAvecDetails, enRetard: Boolean, onFaite: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(libelleActivite(activite.type), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("${activite.cultureNom} — ${activite.parcelleNom}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatDate(activite.datePrevue), fontSize = 14.sp,
                    color = if (enRetard) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Checkbox(checked = activite.faite, onCheckedChange = { onFaite() })
        }
    }
}

private fun libelleActivite(type: TypeActivite): String = when (type) {
    TypeActivite.DESHERBAGE -> "Désherbage"
    TypeActivite.FERTILISATION -> "Fertilisation"
    TypeActivite.RECOLTE -> "Récolte"
}

private fun formatDate(millis: Long): String = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(millis)
