package com.example.composeapp.data.common

import com.example.composeapp.data.categories.mapper.toDomainModel
import com.example.composeapp.data.common.source.RecipesRepositoryStub
import com.example.composeapp.data.recipes.mapper.toDomainModel
import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.common.repository.RecipesRepository
import com.example.composeapp.domain.recipes.model.Recipe

class RecipesRepositoryImpl(
    private val dataSource: RecipesRepositoryStub
) : RecipesRepository {

    override fun getCategories(): List<Category> {
        val categoriesDto = dataSource.getCategories()
        return categoriesDto.map { it.toDomainModel() }
    }

    override fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        val recipesDto = dataSource.getRecipesByCategoryId(categoryId)
        return recipesDto.map { it.toDomainModel() }
    }

    override fun getRecipeById(recipeId: Int): Recipe? {
        val recipeDto = dataSource.getRecipeById(recipeId)
        return recipeDto?.toDomainModel()
    }
}
