package com.example.composeapp.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.ui.theme.Dimens
import com.example.composeapp.ui.theme.RecipesAppTheme
import com.example.composeapp.ui.theme.recipesAppTypography

@Composable
fun ScreenHeader(
    @StringRes titleResId: Int,
    @DrawableRes imageResId: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.headerImageSize)
    ) {
        Image(
            painter = painterResource(imageResId),
            contentDescription = stringResource(R.string.categories_header_image_description),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Title(
            title = stringResource(id = titleResId),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.paddingLarge)
        )
    }
}

@Composable
fun Title(title: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = title.uppercase(),
            style = recipesAppTypography.displayLarge,
            modifier = Modifier.padding(Dimens.paddingBig)
        )
    }
}


@Preview(name = "LightTheme")
@Composable
fun ScreenHeaderLightPreview() {
    RecipesAppTheme {
        ScreenHeader(
            imageResId = R.drawable.categories,
            titleResId = R.string.categories
        )
    }
}

@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScreenHeaderDarkPreview() {
    RecipesAppTheme {
        ScreenHeader(
            imageResId = R.drawable.categories,
            titleResId = R.string.categories
        )
    }
}
