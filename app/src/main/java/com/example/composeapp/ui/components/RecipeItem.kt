package com.example.composeapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composeapp.R
import com.example.composeapp.ui.theme.Dimens
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipeItem(
    imageUri: String,
    title: String,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(Dimens.cornerRadiusMedium)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = Dimens.shadowElevation,
                shape = cardShape
            )
            .clip(cardShape)
            .clickable(onClick = onClick)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_error),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.recipeCardImageHeight),
            contentScale = ContentScale.Crop
        )
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(Dimens.paddingSmall)
        )
    }
}

@Preview(name = "LightTheme")
@Composable
fun RecipeItemLightPreview() {
    RecipesAppTheme {
        RecipeItem(
            imageUri = "",
            title = "Заголовок",
            onClick = {}
        )
    }
}

@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RecipeItemDarkPreview() {
    RecipesAppTheme {
        RecipeItem(
            imageUri = "",
            title = "Заголовок",
            onClick = {}
        )
    }
}
