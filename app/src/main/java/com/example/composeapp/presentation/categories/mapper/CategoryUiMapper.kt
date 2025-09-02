package com.example.composeapp.presentation.categories.mapper

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.presentation.categories.model.CategoryUiModel
import com.example.composeapp.presentation.common.constants.UIConstants.BASE_IMAGE_URL

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = BASE_IMAGE_URL + imageUrl
)
