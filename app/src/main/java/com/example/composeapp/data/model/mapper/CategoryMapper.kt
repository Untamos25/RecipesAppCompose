package com.example.composeapp.data.model.mapper

import com.example.composeapp.data.ASSETS_URI_PREFIX
import com.example.composeapp.data.model.CategoryDto
import com.example.composeapp.ui.model.CategoryUiModel

fun CategoryDto.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)
