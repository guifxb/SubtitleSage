package com.example.subtitlesage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.subtitlesage.R
import com.example.subtitlesage.model.MovieInfoLocal
import com.example.subtitlesage.ui.theme.AppTheme

@Composable
fun DetailScreen(
    movieInfoLocal: MovieInfoLocal,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        PosterAndInfo(movieInfoLocal = movieInfoLocal)
        PlotAndLink(movieInfoLocal = movieInfoLocal)
    }
}

@Composable
private fun PosterAndInfo(
    movieInfoLocal: MovieInfoLocal,
) {
    Row {
        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
            .data(movieInfoLocal.poster).crossfade(true).build(),
            contentDescription = movieInfoLocal.title,
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            modifier = Modifier
                .size(AppTheme.dimens.posterX, AppTheme.dimens.posterY)
                .padding(AppTheme.dimens.medium))
        InfoCard(movieInfoLocal = movieInfoLocal)
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    movieInfoLocal: MovieInfoLocal,
) {
    Column(Modifier.padding(top = AppTheme.dimens.large), horizontalAlignment = Alignment.Start) {
        Text(text = movieInfoLocal.title,
            modifier = modifier.padding(top = AppTheme.dimens.large, start = AppTheme.dimens.medium),
            style = MaterialTheme.typography.headlineLarge)
        Text(text = "(${movieInfoLocal.year})",
            modifier = modifier.padding(top = AppTheme.dimens.large, start = AppTheme.dimens.medium),
            style = MaterialTheme.typography.headlineMedium)

    }
}

@Composable
fun PlotAndLink(
    movieInfoLocal: MovieInfoLocal,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(AppTheme.dimens.medium)) {
        Text(
            text = movieInfoLocal.plot,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
        )
        Spacer(modifier = Modifier.size(AppTheme.dimens.large * 2))
        AnnotatedClickableText(movieInfoLocal = movieInfoLocal)
    }
}

@Composable
fun AnnotatedClickableText(
    movieInfoLocal: MovieInfoLocal,
) {
    val movieUrl = "https://www.imdb.com/title/${movieInfoLocal.imdbid}/"
    val mUriHandler = LocalUriHandler.current
    val style = MaterialTheme.typography.bodyMedium.toSpanStyle()
    val annotatedText = buildAnnotatedString {
        withStyle(style) {
            append(stringResource(R.string.see_on))
        }
        withStyle(style.copy(color = Color.Blue)) {
            appendLink("IMdb", movieUrl)
        }
    }

    ClickableText(text = annotatedText, onClick = {
        annotatedText.getStringAnnotations(movieUrl, it, it).firstOrNull()
            ?.let { stringAnnotation ->
                mUriHandler.openUri(stringAnnotation.item)
            }
    })
}

fun AnnotatedString.Builder.appendLink(linkText: String, linkUrl: String) {
    pushStringAnnotation(tag = linkUrl, annotation = linkUrl)
    append(linkText)
    pop()
}