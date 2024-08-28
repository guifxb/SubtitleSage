package com.gfbdev.subtitlesage.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.gfbdev.subtitlesage.R
import com.gfbdev.subtitlesage.model.UserInfo
import com.gfbdev.subtitlesage.model.defaultUser
import com.gfbdev.subtitlesage.ui.theme.AppTheme
import com.gfbdev.subtitlesage.ui.theme.PortfolioTheme
import com.gfbdev.subtitlesage.ui.theme.rememberWindowsSizeClass

enum class ContactType {
    WhatsApp, LinkedIn, Email
}

@Composable
fun HomeScreen(
    onPortfolioButtonClicked: () -> Unit,
    onAboutButtonClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mainUser: UserInfo,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )

    ) {
        val isDefault = mainUser == defaultUser

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = Modifier.padding(AppTheme.dimens.medium),
            elevation = CardDefaults.outlinedCardElevation(AppTheme.dimens.medium)) {
            Column(verticalArrangement = Arrangement.Top) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(AppTheme.dimens.medium)
                            .alpha(if (isDefault) 0.5f else 1f),
                        text = if (isDefault) stringResource(R.string.default_name) else mainUser.name,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(AppTheme.dimens.medium)
                            .alpha(if (isDefault) 0.5f else 1f),
                        text = if (isDefault) stringResource(R.string.professional_title) else mainUser.title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                Column(verticalArrangement = Arrangement.Bottom) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = AppTheme.dimens.medium),
                        text = stringResource(R.string.about_me),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = AppTheme.dimens.medium, end = AppTheme.dimens.medium)
                            .alpha(if (isDefault) 0.5f else 1f),
                        text = if (isDefault) stringResource(R.string.short_summary) else mainUser.aboutMe,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify                        
                    )
                    Spacer(modifier = modifier.size(AppTheme.dimens.large * (5 - mainUser.aboutMe.length / 100)))
                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.medium),
                    horizontalArrangement = Arrangement.End ) {
                        IconButton(onClick = { onAboutButtonClicked(true) }) {
                            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                        }
                    }
                }
            }
        }

        ContactInfo(user = mainUser)

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .padding(AppTheme.dimens.medium)
                    .wrapContentWidth()
                    .fillMaxWidth(0.5f), onClick = onPortfolioButtonClicked
            ) {
                Text(
                    stringResource(R.string.portfolio_button),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                modifier = Modifier
                    .padding(AppTheme.dimens.medium)
                    .wrapContentWidth()
                    .fillMaxWidth(), onClick = { onAboutButtonClicked(false) }
            ) {
                Text(
                    text = stringResource(R.string.experience_button),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Composable
fun ContactInfo(
    modifier: Modifier = Modifier,
    user: UserInfo,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(AppTheme.dimens.medium),
        elevation = CardDefaults.outlinedCardElevation(AppTheme.dimens.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.medium)

            ) {
                Text(
                    modifier = modifier.align(Alignment.CenterVertically),
                    text = stringResource(R.string.contact),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                ContactIconButton(
                    expanded = expanded,
                    onClick = {
                        expanded = !expanded
                    },
                )
            }
        }
        if (expanded) {
            enumValues<ContactType>().forEach { InfoCard(info = it, user = user) }
        }
    }
}

@Composable
fun InfoCard(info: ContactType, user: UserInfo) {
    Row(modifier = Modifier.padding(AppTheme.dimens.medium)) {
        Icon(
            imageVector = when (info) {
                ContactType.Email -> Icons.Filled.Email
                ContactType.WhatsApp -> Icons.Filled.Phone
                ContactType.LinkedIn -> Icons.Filled.ContactPage
            }, contentDescription = null
        )
        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = when (info) {
                ContactType.Email -> user.email
                ContactType.WhatsApp -> user.whatsApp
                ContactType.LinkedIn -> user.linkedIn
            }, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ContactIconButton(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = null
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val window = rememberWindowsSizeClass()
    PortfolioTheme(window) {
        HomeScreen(
            onPortfolioButtonClicked = { },
            onAboutButtonClicked = { },
            mainUser = defaultUser
        )
    }
}