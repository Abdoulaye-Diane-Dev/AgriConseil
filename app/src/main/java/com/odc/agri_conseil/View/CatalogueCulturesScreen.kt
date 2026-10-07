package com.orangedigitalcenter.agriconseil.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odc.agri_conseil.Model.Local.Entity.Culture
import com.odc.agri_conseil.R
import com.odc.agri_conseil.View.AppHeader
import com.odc.agri_conseil.View.AppHeaderSecond
import com.odc.agri_conseil.View.ScrollFadeOverlay
import com.odc.agri_conseil.ViewModel.CatalogueCulturesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueCulturesScreen(viewModel: CatalogueCulturesViewModel) {
    val cultures by viewModel.cultures.collectAsState()
    val recherche by viewModel.recherche.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val categorieSelectionnee by viewModel.categorieSelectionnee.collectAsState()
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeaderSecond(title = "Catalogues des cultures")

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = recherche,
                onValueChange = viewModel::onRechercheChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Rechercher une culture...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = categorieSelectionnee == null,
                        onClick = { viewModel.onCategorieChange(null) },
                        label = { Text("Toutes") }
                    )
                }
                items(categories) { categorie ->
                    FilterChip(
                        selected = categorieSelectionnee == categorie,
                        onClick = { viewModel.onCategorieChange(categorie) },
                        label = { Text(categorie) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (cultures.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune donnée pour le moment", fontSize = 14.sp)
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cultures, key = { it.id }) { culture -> CultureCard(culture) }
                    }
                    ScrollFadeOverlay(listState, Modifier.align(Alignment.TopCenter))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CultureCard(culture: Culture) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = idImageCulture(culture.nom)),
                contentDescription = culture.nom,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(culture.nom, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    AssistChip(onClick = {}, label = { Text(culture.categorie, fontSize = 12.sp) })
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Semis : ${culture.moisSemis}", fontSize = 14.sp)
                Text("Cycle : ${culture.dureeCycleJours} jours", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(culture.conseils, fontSize = 14.sp)
            }
        }
    }
}

private fun idImageCulture(nom: String): Int = when (nom) {
    "Riz" -> R.drawable.riz
    "Manioc" -> R.drawable.manioc
    "Arachide" -> R.drawable.arachide
    "Fonio" -> R.drawable.fonio
    "Maïs" -> R.drawable.mais
    else -> R.drawable.ic_leaf_logo
}

