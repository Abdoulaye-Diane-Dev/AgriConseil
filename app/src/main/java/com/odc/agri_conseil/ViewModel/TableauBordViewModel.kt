package com.odc.agri_conseil.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odc.agri_conseil.Model.Local.Entity.ActiviteAvecDetails
import com.odc.agri_conseil.Model.repository.AgriRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TableauBordViewModel(private val repository: AgriRepository) : ViewModel() {

    private val maintenant = System.currentTimeMillis()

    val activitesAVenir: StateFlow<List<ActiviteAvecDetails>> = repository
        .getActivitesAVenir(maintenant)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activitesEnRetard: StateFlow<List<ActiviteAvecDetails>> = repository
        .getActivitesEnRetard(maintenant)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nombreParcelles: StateFlow<Int> = repository.getParcelles()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val nombrePlantations: StateFlow<Int> = repository.getPlantationsActivesAvecCulture()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun marquerFaite(activiteId: Long) {
        viewModelScope.launch { repository.marquerActiviteFaite(activiteId) }
    }
}
