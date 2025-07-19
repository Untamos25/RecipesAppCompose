package com.example.composeapp.data.categories.mapper

import com.example.composeapp.data.categories.model.CategoryDto
import com.example.composeapp.data.common.ASSETS_URI_PREFIX
import com.example.composeapp.domain.categories.model.Category

fun CategoryDto.toDomainModel() = Category(
    id = id,
    title = title,
    description = description,
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)
