package com.example.subtitlesage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.subtitlesage.R
import com.example.subtitlesage.model.BrokenTitle
import com.example.subtitlesage.model.DefaultTitleToAdd
import com.example.subtitlesage.model.MovieInfoNet
import com.example.subtitlesage.ui.theme.AppTheme


@Composable
fun AddTitleScreen(
    addTitleCurrentMovie: MovieInfoNet,
    modifier: Modifier = Modifier,
    onSearchButtonClicked: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        PosterAndInfo(movieInfoLocal = addTitleCurrentMovie)
        Plot(movieInfoLocal = addTitleCurrentMovie)
        Spacer(modifier.weight(1f, true))
        SaveButtons(
            movieInfoLocal = addTitleCurrentMovie,
            onSearchButtonClicked = onSearchButtonClicked,
            onAddButtonClicked = onAddButtonClicked
        )
    }
}

@Composable
private fun PosterAndInfo(
    movieInfoLocal: MovieInfoNet,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(movieInfoLocal.poster)
                .crossfade(true).build(),
            contentDescription = movieInfoLocal.title,
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            modifier = Modifier
                .size(AppTheme.dimens.posterX, AppTheme.dimens.posterY)
                .padding(AppTheme.dimens.medium)
        )
        InfoCard(movieInfoLocal = movieInfoLocal)
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    movieInfoLocal: MovieInfoNet,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimens.large), horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = movieInfoLocal.title,
            modifier = modifier.padding(top = AppTheme.dimens.large, start = AppTheme.dimens.medium),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "(${movieInfoLocal.year})",
            modifier = modifier.padding(top = AppTheme.dimens.large, start = AppTheme.dimens.medium),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun Plot(
    movieInfoLocal: MovieInfoNet,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(AppTheme.dimens.large)) {
        Text(
            text = movieInfoLocal.plot,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
        )
        Spacer(modifier = Modifier.size(AppTheme.dimens.large))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveButtons(
    modifier: Modifier = Modifier,
    movieInfoLocal: MovieInfoNet,
    onSearchButtonClicked: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
) {
    val savedToast = Toast.makeText(
        LocalContext.current, stringResource(R.string.title_save_toast), Toast.LENGTH_LONG
    )
    val errorToast = Toast.makeText(
        LocalContext.current, stringResource(R.string.title_not_found_toast), Toast.LENGTH_LONG
    )
    var openDialogAdd by remember { mutableStateOf(false) }
    var textField by remember { mutableStateOf("") }
    var saveButtonBool by remember { mutableStateOf(false) }
    var showHelp by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier
            .padding(AppTheme.dimens.large)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = textField,
            onValueChange = { textField = it },
            label = { Text(stringResource(R.string.imdb_id_or_url_button_label)) },
            placeholder = {
                Text(
                    text = stringResource(R.string.example_placeholder),
                    modifier = Modifier.alpha(0.35f)
                )
            },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            supportingText = {
                Text(
                    text = stringResource(R.string.add_button_helper),
                    modifier = Modifier
                        .alpha(0.35f)
                        .clickable { showHelp = !showHelp },
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline)
                )
            })
        Row(
            modifier
                .padding(AppTheme.dimens.large)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onSearchButtonClicked(textField)
                if (movieInfoLocal == BrokenTitle) {
                    errorToast.show()
                }
            }) { Text(text = stringResource(R.string.search_button)) }

            Button(onClick = { openDialogAdd = true }, enabled = saveButtonBool) {
                Text(text = stringResource(R.string.save_button))
            }
        }
    }

    saveButtonBool = movieInfoLocal != DefaultTitleToAdd && movieInfoLocal != BrokenTitle
    if (showHelp) {
        Dialog(onDismissRequest = { showHelp = !showHelp }) {
            ShowHelp(onOkClicked = { showHelp = !showHelp })
        }
    }


    if (openDialogAdd) {
        AlertDialog(onDismissRequest = {
            openDialogAdd = false
        }, title = {
            Text(text = stringResource(id = R.string.add_title_alert_title))
        }, text = {
            Text(text = stringResource(R.string.add_title_alert_text) + movieInfoLocal.title + "?")
        }, confirmButton = {
            TextButton(onClick = {
                onAddButtonClicked()
                openDialogAdd = false
                savedToast.show()
                textField = ""
            }) {
                Text(stringResource(id = R.string.just_yes))
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialogAdd = false
            }) {
                Text(stringResource(id = R.string.just_no))
            }
        })
    }
}

@Composable
fun ShowHelp(onOkClicked: () -> Unit) {
    Card {
        Column(
            modifier = Modifier.padding(AppTheme.dimens.smallMedium),
            verticalArrangement = Arrangement.SpaceEvenly) {
            Spacer(modifier = Modifier.size(AppTheme.dimens.large + 20.dp))
            ClickableText()
            Spacer(modifier = Modifier.size(AppTheme.dimens.medium))
            Text(text = stringResource(R.string.add_title_help_text_2),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black))
            Spacer(modifier = Modifier.size(AppTheme.dimens.medium))
            Image(painter = painterResource(id = R.drawable.image_add_example),
                modifier = Modifier.size(AppTheme.dimens.posterY + 50.dp, AppTheme.dimens.posterX - 50.dp),
                contentScale = ContentScale.FillBounds,
                contentDescription = null)
            Spacer(modifier = Modifier.size(AppTheme.dimens.medium))
            Text(text = stringResource(R.string.add_title_help_text_3),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black))
            Spacer(modifier = Modifier.size(AppTheme.dimens.large + 20.dp))
            Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()){
                Button(onClick = onOkClicked) {
                    Text(text = stringResource(R.string.just_okay))
                }
            }
        }
    }
}

@Composable
fun ClickableText(
) {
    val style = MaterialTheme.typography.bodyLarge.toSpanStyle()
    val url = "https://www.imdb.com/"
    val mUriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        withStyle(style) {
            append(stringResource(R.string.add_title_help_text_1))
        }
        withStyle(style.copy(color = Color.Blue)) {
            appendLink(stringResource(R.string.click_here), url)
        }
    }
    ClickableText(text = annotatedText, onClick = {
        annotatedText.getStringAnnotations(url, it, it).firstOrNull()?.let { stringAnnotation ->
            mUriHandler.openUri(stringAnnotation.item)
        }
    })
}


