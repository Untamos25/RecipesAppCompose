package com.pavlushinsa.recipescompapp.presentation.categories.mapper

import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.presentation.categories.model.CategoryUiModel
import com.pavlushinsa.recipescompapp.presentation.common.constants.UIConstants.BASE_IMAGE_URL

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = BASE_IMAGE_URL + imageUrl
)
