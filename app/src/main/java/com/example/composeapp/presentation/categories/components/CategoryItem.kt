package com.example.composeapp.presentation.categories.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composeapp.R
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme

@Composable
fun CategoryItem(
    imageUri: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(Dimens.cornerRadiusMedium)

    Column(
        modifier = Modifier
            .size(height = Dimens.categoryCardHeight, width = Dimens.categoryCardWidth)
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
                .height(Dimens.categoryCardImageHeight),
            contentScale = ContentScale.Crop
        )
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(Dimens.paddingSmall)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(
                start = Dimens.paddingSmall,
                end = Dimens.paddingSmall,
                bottom = Dimens.paddingSmall
            )
        )
    }
}

@Preview(name = "LightTheme")
@Composable
fun CategoryItemLightPreview() {
    RecipesAppTheme {
        CategoryItem(
            imageUri = "",
            title = "Заголовок",
            description = "Описание на несколько строк на карточке",
            onClick = {}
        )
    }
}

@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryItemDarkPreview() {
    RecipesAppTheme {
        CategoryItem(
            imageUri = "",
            title = "Заголовок",
            description = "Описание на несколько строк на карточке",
            onClick = {}
        )
    }
}
