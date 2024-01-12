package com.example.recipeapp.repository

import androidx.lifecycle.LiveData
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) {
    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }
}
