// MainActivity.kt in the ui package
package com.example.recipeapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.ui.theme.RecipeappTheme
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(application).recipeDao()
        val repository = RecipeRepository(dao)
        val viewModelFactory = RecipeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        setContent {
            RecipeappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeScreen(viewModel)
                }
            }
        }

        // Inserting sample recipes - only do this once, you might want to control this better
        val sampleRecipes = listOf(
            Recipe("Pancakes", "Flour, Eggs, Milk", "Mix and fry"),
            // Add 9 more recipes
        )
        sampleRecipes.forEach { viewModel.insert(it) }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeScreen(viewModel: RecipeViewModel) {
    var searchText by remember { mutableStateOf("") }
    val recipes by viewModel.allRecipes.collectAsState(initial = emptyList())
    val filteredRecipes = recipes.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Input Field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Recipes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Recipes List
        LazyColumn {
            items(filteredRecipes) { recipe ->
                RecipeItem(recipe)
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = recipe.title, style = MaterialTheme.typography.h6)
            Text(text = "Ingredients: ${recipe.ingredients}", style = MaterialTheme.typography.body1)
            Text(text = "Instructions: ${recipe.instructions}", style = MaterialTheme.typography.body1)
        }
    }
}

