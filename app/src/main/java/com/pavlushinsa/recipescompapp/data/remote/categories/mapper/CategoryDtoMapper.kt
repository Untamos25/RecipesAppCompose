package com.pavlushinsa.recipescompapp.data.remote.categories.mapper

import com.pavlushinsa.recipescompapp.data.local.categories.entity.CategoryEntity
import com.pavlushinsa.recipescompapp.data.remote.categories.model.CategoryDto

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)
