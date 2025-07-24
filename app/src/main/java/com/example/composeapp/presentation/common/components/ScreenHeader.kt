package com.example.composeapp.presentation.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composeapp.R
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme

@Composable
fun ScreenHeader(
    title: String? = null,
    @StringRes titleResId: Int? = null,
    imageUrl: String? = null,
    @DrawableRes imageResId: Int? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.headerImageSize)
    ) {
        val imageModel = imageUrl ?: imageResId

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.image_description_categories_header),
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_error),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        val headerTitle = title ?: (titleResId?.let { stringResource(id = it) } ?: "")

        Title(
            title = headerTitle,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.paddingLarge)
        )
    }
}

@Composable
private fun Title(title: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = title.uppercase(),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(Dimens.paddingMedium)
        )
    }
}


@Preview
@Preview(locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenHeaderPreview() {
    RecipesAppTheme {
        ScreenHeader(
            imageResId = R.drawable.img_header_categories,
            titleResId = R.string.title_categories
        )
    }
}
