package com.odc.agri_conseil.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.odc.agri_conseil.Model.repository.AgriRepository

class AgriViewModelFactory(private val repository: AgriRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(CatalogueCulturesViewModel::class.java) ->
            CatalogueCulturesViewModel(repository) as T

        modelClass.isAssignableFrom(ParcellesViewModel::class.java) ->
            ParcellesViewModel(repository) as T

        modelClass.isAssignableFrom(PlantationFormViewModel::class.java) ->
            PlantationFormViewModel(repository) as T

        modelClass.isAssignableFrom(TableauBordViewModel::class.java) ->
            TableauBordViewModel(repository) as T

        else -> throw IllegalArgumentException("ViewModel inconnu : ${modelClass.name}")
    }
}