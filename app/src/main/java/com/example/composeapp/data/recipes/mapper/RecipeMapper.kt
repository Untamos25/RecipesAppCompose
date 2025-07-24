package com.example.composeapp.data.recipes.mapper

import com.example.composeapp.data.recipes.model.RecipeDto
import com.example.composeapp.domain.recipes.model.Recipe

fun RecipeDto.toDomainModel() = Recipe(
    id = id,
    title = title,
    ingredients = ingredients.map { it.toDomainModel() },
    method = method,
    imageUrl = imageUrl
)
