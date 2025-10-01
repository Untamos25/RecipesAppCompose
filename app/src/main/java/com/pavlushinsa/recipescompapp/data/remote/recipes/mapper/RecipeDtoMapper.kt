package com.pavlushinsa.recipescompapp.data.remote.recipes.mapper

import com.pavlushinsa.recipescompapp.data.local.recipes.entity.RecipeEntity
import com.pavlushinsa.recipescompapp.data.remote.recipes.model.RecipeDto
import com.pavlushinsa.recipescompapp.data.remote.constants.ApiConstants.BASE_IMAGE_URL

fun RecipeDto.toEntity(categoryId: Int) = RecipeEntity(
    id = id,
    categoryId = categoryId,
    title = title,
    method = method,
    imageUrl = BASE_IMAGE_URL + imageUrl
)
