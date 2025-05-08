package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.ui.theme.ComposeAppTheme
import com.example.composeapp.ui.theme.recipesAppTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAppTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Заголовок (displayLarge)",
                style = recipesAppTypography.displayLarge
            )
            Text(
                text = "Заголовок средний (titleMedium)",
                style = recipesAppTypography.titleMedium
            )
            Text(
                text = "Тело средний (bodyMedium)",
                style = recipesAppTypography.bodyMedium
            )
            Text(
                text = "Тело маленький (bodySmall)",
                style = recipesAppTypography.bodySmall
            )
            Text(
                text = "лейбл (labelLarge)",
                style = recipesAppTypography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeAppTheme {
        Greeting()
    }
}