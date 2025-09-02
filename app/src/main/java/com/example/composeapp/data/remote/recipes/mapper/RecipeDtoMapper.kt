package com.example.composeapp.data.remote.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.RecipeEntity
import com.example.composeapp.data.remote.recipes.model.RecipeDto
import com.example.composeapp.presentation.common.constants.UIConstants.BASE_IMAGE_URL

fun RecipeDto.toEntity(categoryId: Int) = RecipeEntity(
    id = id,
    categoryId = categoryId,
    title = title,
    method = method,
    imageUrl = BASE_IMAGE_URL + imageUrl
)
