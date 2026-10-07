package com.odc.agri_conseil.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.Model.repository.AgriRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


class CatalogueCulturesViewModel(repository: AgriRepository) : ViewModel() {

    private val toutesLesCultures: StateFlow<List<Culture>> = repository.getCultures()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _recherche = MutableStateFlow("")
    val recherche: StateFlow<String> = _recherche.asStateFlow()

    private val _categorieSelectionnee = MutableStateFlow<String?>(null)
    val categorieSelectionnee: StateFlow<String?> = _categorieSelectionnee.asStateFlow()

    val categories: StateFlow<List<String>> = toutesLesCultures
        .map { cultures -> cultures.map { it.categorie }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cultures: StateFlow<List<Culture>> = combine(
        toutesLesCultures, _recherche, _categorieSelectionnee
    ) { toutes, recherche, categorie ->
        toutes.filter { culture ->
            val correspondNom = culture.nom.contains(recherche, ignoreCase = true)
            val correspondCategorie = categorie == null || culture.categorie == categorie
            correspondNom && correspondCategorie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onRechercheChange(valeur: String) { _recherche.value = valeur }
    fun onCategorieChange(categorie: String?) { _categorieSelectionnee.value = categorie }
}