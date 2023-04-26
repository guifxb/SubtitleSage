package com.example.translatorsportfolio.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.translatorsportfolio.R
import com.example.translatorsportfolio.model.UserInfo
import com.example.translatorsportfolio.model.defaultUser
import com.example.translatorsportfolio.ui.theme.AppTheme
import com.example.translatorsportfolio.ui.theme.PortfolioTheme
import com.example.translatorsportfolio.ui.theme.rememberWindowsSizeClass

enum class ContactType() {
    WhatsApp, LinkedIn, Email
}

@Composable
fun HomeScreen(
    onPortfolioButtonClicked: () -> Unit,
    onAboutButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    mainUser: UserInfo,
    ) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.medium),
                text = mainUser.name,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.medium),
                text = mainUser.title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = AppTheme.dimens.medium),
                text = stringResource(R.string.about_me),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = AppTheme.dimens.medium, end = AppTheme.dimens.medium),
                text = mainUser.aboutMe,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
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
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                modifier = Modifier
                    .padding(AppTheme.dimens.medium)
                    .wrapContentWidth()
                    .fillMaxWidth(), onClick = onAboutButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.experience_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ContactInfo(modifier: Modifier = Modifier,
    user: UserInfo) {
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
        HomeScreen(onPortfolioButtonClicked = { }, onAboutButtonClicked = { }, mainUser = defaultUser)
    }
}