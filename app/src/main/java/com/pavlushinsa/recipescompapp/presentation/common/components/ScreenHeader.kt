package com.pavlushinsa.recipescompapp.presentation.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme

@Composable
fun ScreenHeader(
    contentDescription: String,
    title: String? = null,
    imageUrl: String? = null,
    @StringRes titleResId: Int? = null,
    @DrawableRes imageResId: Int? = null

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(Dimens.HEADER_IMAGE_ASPECT_RATIO)
    ) {
        val imageModel = imageUrl ?: imageResId

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_error),
            modifier = Modifier.matchParentSize(),
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
            titleResId = R.string.title_categories,
            contentDescription = ""
        )
    }
}
