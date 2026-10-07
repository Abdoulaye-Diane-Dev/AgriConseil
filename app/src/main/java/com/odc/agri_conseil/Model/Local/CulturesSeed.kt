package com.odc.agri_conseil.Model.Local

import com.odc.agri_conseil.Model.Local.Entity.Culture

object CultureSeed {
    val data = listOf(
        Culture(nom = "Riz", categorie = "Céréale", moisSemis = "Mai à Juillet", dureeCycleJours = 120,
            conseils = "Semer en début de saison des pluies. Désherber 2 à 3 fois avant la floraison. Éviter les parcelles mal drainées pour le riz pluvial."),
        Culture(nom = "Manioc", categorie = "Racine", moisSemis = "Mars à Mai", dureeCycleJours = 300,
            conseils = "Planter des boutures saines en début de saison des pluies. Butter la terre autour des plants après 2 mois. Récolte possible entre 8 et 12 mois."),
        Culture(nom = "Arachide", categorie = "Légumineuse", moisSemis = "Mai à Juin", dureeCycleJours = 100,
            conseils = "Semer sur sol léger et bien drainé. Sarcler avant la floraison. Éviter l'excès d'eau en fin de cycle."),
        Culture(nom = "Fonio", categorie = "Céréale", moisSemis = "Juin à Juillet", dureeCycleJours = 100,
            conseils = "Céréale rustique adaptée aux sols pauvres. Semis à la volée. Récolter dès que les grains commencent à s'égrener."),
        Culture(nom = "Maïs", categorie = "Céréale", moisSemis = "Avril à Juin", dureeCycleJours = 100,
            conseils = "Semer en poquets espacés de 80 cm. Fertiliser à la levée puis à la montaison. Sensible au stress hydrique en floraison.")
    )
}