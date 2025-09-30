package com.pavlushinsa.recipescompapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pavlushinsa.recipescompapp.data.local.categories.entity.CategoryEntity
import com.pavlushinsa.recipescompapp.data.local.categories.dao.CategoryDao
import com.pavlushinsa.recipescompapp.data.local.recipes.entity.IngredientEntity
import com.pavlushinsa.recipescompapp.data.local.recipes.entity.RecipeEntity
import com.pavlushinsa.recipescompapp.data.local.recipes.converters.MethodConverter
import com.pavlushinsa.recipescompapp.data.local.recipes.dao.RecipeDao

@Database(
    entities = [
        CategoryEntity::class,
        RecipeEntity::class,
        IngredientEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MethodConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}
