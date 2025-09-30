package com.pavlushinsa.recipescompapp.domain.recipes.model

data class Recipe(
    val id: Int,
    val categoryId: Int,
    val title: String,
    val method: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean,
    val lastSyncTime: Long
)
