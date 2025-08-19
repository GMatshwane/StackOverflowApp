package com.gordon.stackoverflowapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gordon.stackoverflow.ui.DetailScreen
import com.gordon.stackoverflow.ui.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "search") {
                        composable("search") {
                            SearchScreen(navController = navController)
                        }
                        composable("detail/{questionId}") { backStackEntry ->
                            val questionId = backStackEntry.arguments?.getString("questionId")?.toIntOrNull() ?: 0
                            DetailScreen(questionId = questionId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}