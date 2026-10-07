package com.odc.agri_conseil.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.repository.AgriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class PlantationFormUiState(
    val cultures: List<Culture> = emptyList(),
    val parcelles: List<Parcelle> = emptyList(),
    val error: String? = null,
    val success: Boolean = false
)

class PlantationFormViewModel(private val repository: AgriRepository) : ViewModel() {
    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(success = false)
    }
    private val _uiState = MutableStateFlow(PlantationFormUiState())
    val uiState: StateFlow<PlantationFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.getCultures(), repository.getParcelles()) { cultures, parcelles ->
                cultures to parcelles
            }.collect { (cultures, parcelles) ->
                _uiState.value = _uiState.value.copy(cultures = cultures, parcelles = parcelles)
            }
        }
    }

    fun creerPlantation(parcelleId: Long?, cultureId: Long?, dateSemis: Long) {
        if (parcelleId == null || cultureId == null) {
            _uiState.value = _uiState.value.copy(error = "Choisis une parcelle et une culture.")
            return
        }
        viewModelScope.launch {
            repository.creerPlantation(parcelleId, cultureId, dateSemis)
            _uiState.value = _uiState.value.copy(success = true, error = null)
        }
    }
}
