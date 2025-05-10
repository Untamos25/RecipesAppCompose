package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.ui.theme.RecipesAppTheme
import com.example.composeapp.ui.theme.recipesAppTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesAppTheme {
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

            Button(
                onClick = { }
            ) {
                Text("КАТЕГОРИИ")
            }

            HorizontalDivider(thickness = 5.dp)

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text("ИЗБРАННОЕ")
            }

            ColorDemonstration(
                text = "Цвет ползунка прогресса порций",
                backgroundColor = MaterialTheme.colorScheme.tertiary,
            )

            ColorDemonstration(
                text = "Цвет слайдера порций",
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            )

            ColorDemonstration(
                text = "Фон карточек и разделителей",
                backgroundColor = MaterialTheme.colorScheme.surface,
            )

            ColorDemonstration(
                text = "Разделители и неактивные элементы",
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
fun ColorDemonstration(text: String, backgroundColor: androidx.compose.ui.graphics.Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall
        )
        Surface(
            color = backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) { }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Composable
fun GreetingPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        Greeting()
    }
}

@Preview(name = "DarkTheme", showBackground = true)
@Composable
fun GreetingPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        Greeting()
    }
}
