package com.odc.agri_conseil.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odc.agri_conseil.Model.Local.Entity.Activite
import com.odc.agri_conseil.Model.Local.Entity.Parcelle
import com.odc.agri_conseil.Model.Local.Entity.PlantationActiveAvecCulture
import com.odc.agri_conseil.Model.repository.AgriRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ParcellesViewModel(private val repository: AgriRepository) : ViewModel() {

    val parcelles: StateFlow<List<Parcelle>> = repository.getParcelles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Plantations en cours, regroupées par parcelle : la vue n'a qu'à lire
    // plantationsParParcelle[parcelle.id] pour afficher ce qui pousse dessus.
    val plantationsParParcelle: StateFlow<Map<Long, List<PlantationActiveAvecCulture>>> =
        repository.getPlantationsActivesAvecCulture()
            .map { plantations -> plantations.groupBy { it.parcelleId } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Historique des activités terminées d'une plantation précise, chargé à la demande
    // (seulement quand la ligne de la plantation est dépliée dans l'écran).
    fun activitesFaitesPourPlantation(plantationId: Long): Flow<List<Activite>> =
        repository.getActivitesFaitesParPlantation(plantationId)

    fun ajouterParcelle(nom: String, superficieHa: Double, localisation: String) {
        viewModelScope.launch {
            repository.creerParcelle(Parcelle(nom = nom, superficieHa = superficieHa, localisation = localisation))
        }
    }

    fun supprimerParcelle(parcelle: Parcelle) {
        viewModelScope.launch {
            repository.supprimerParcelle(parcelle)
        }
    }
}

