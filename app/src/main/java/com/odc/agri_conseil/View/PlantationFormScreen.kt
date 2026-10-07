package com.orangedigitalcenter.agriconseil.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.View.AppHeader
import com.odc.agri_conseil.View.AppHeaderSecond
import com.odc.agri_conseil.ViewModel.PlantationFormViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantationFormScreen(viewModel: PlantationFormViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var cultureSelectionnee by remember { mutableStateOf<Culture?>(null) }
    var parcelleSelectionnee by remember { mutableStateOf<Parcelle?>(null) }
    var dateSemisMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            cultureSelectionnee = null
            parcelleSelectionnee = null
            dateSemisMillis = System.currentTimeMillis()
            delay(2000)
            viewModel.resetSuccess()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeaderSecond(title = "Nouvelle plantation")

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DropdownCulture(uiState.cultures, cultureSelectionnee) { cultureSelectionnee = it }
            DropdownParcelle(uiState.parcelles, parcelleSelectionnee) { parcelleSelectionnee = it }

            OutlinedTextField(
                value = formatDate(dateSemisMillis),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date de semis") },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (up != null) {
                                showDatePicker = true
                            }
                        }
                    }
            )

            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }
            if (uiState.success) {
                Text("Plantation enregistrée avec son calendrier !", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }

            Button(
                onClick = { viewModel.creerPlantation(parcelleSelectionnee?.id, cultureSelectionnee?.id, dateSemisMillis) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Démarrer le suivi")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateSemisMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateSemisMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Annuler") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownCulture(cultures: List<Culture>, selection: Culture?, onSelect: (Culture) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selection?.nom ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Culture") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            cultures.forEach { culture ->
                DropdownMenuItem(text = { Text(culture.nom) }, onClick = { onSelect(culture); expanded = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownParcelle(parcelles: List<Parcelle>, selection: Parcelle?, onSelect: (Parcelle) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selection?.nom ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Parcelle") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            parcelles.forEach { parcelle ->
                DropdownMenuItem(text = { Text(parcelle.nom) }, onClick = { onSelect(parcelle); expanded = false })
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(millis)