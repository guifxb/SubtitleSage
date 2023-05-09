package com.example.translatorsportfolio.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.translatorsportfolio.model.Experience
import com.example.translatorsportfolio.model.UserInfo
import com.example.translatorsportfolio.ui.theme.AppTheme


@Composable
fun PersonalInfoScreen(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: (UserInfo) -> Unit,
    onAddExperience: (Experience) -> Unit,
    onDeleteExperience: (Experience) -> Unit,
    mainUser: UserInfo,
    experiences: List<Experience>,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Card(
            modifier = modifier.padding(AppTheme.dimens.medium),
            elevation = CardDefaults.outlinedCardElevation(AppTheme.dimens.medium)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                )
            ) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    ContactIconButton(
                        expanded = expanded,
                        onClick = {
                            expanded = !expanded
                        },
                    )
                }
                if (expanded) {
                    UserTab(modifier = Modifier,
                        mainUser = mainUser,
                        onSaveButtonClicked = { onSaveButtonClicked(mainUser) })
                }
            }
        }
        Divider(modifier.padding(AppTheme.dimens.medium),
            thickness = AppTheme.dimens.smallMedium)

        LazyColumn(
            modifier = modifier
        ) {
            items(experiences, key = { it.company }) { experience ->
                ExperienceBlock(experience,
                    mustHaveNext = experiences.last() == experience,
                    onAdd = { onAddExperience(experience) },
                    onDelete = { onDeleteExperience(experience) })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceBlock(
    experience: Experience,
    mustHaveNext: Boolean,
    onAdd: (Experience) -> Unit,
    onDelete: (Experience) -> Unit,
) {
    val savedToast = Toast.makeText(LocalContext.current, "Saved", Toast.LENGTH_LONG)
    val deletedToast = Toast.makeText(LocalContext.current, "Deleted", Toast.LENGTH_LONG)
    val focusManager = LocalFocusManager.current
    var localExperience by remember { mutableStateOf(experience) }

    Column() {
        OutlinedTextField(enabled = mustHaveNext,
            value = localExperience.company,
            onValueChange = { value ->
                localExperience = localExperience.copy(company = value)
                experience.company = value
            },
            label = { Text("Company Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium)
        )

        Row {
            OutlinedTextField(enabled = mustHaveNext,
                value = localExperience.pair,
                onValueChange = { value ->
                    localExperience = localExperience.copy(pair = value)
                    experience.pair = value
                },
                label = { Text("Translation Pair(s)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .padding(AppTheme.dimens.medium)
            )

            OutlinedTextField(enabled = mustHaveNext,
                value = localExperience.role,
                onValueChange = { value ->
                    localExperience = localExperience.copy(role = value)
                    experience.role = value
                },
                label = { Text("Professional Role") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.medium),
                placeholder = {
                    Text(
                        text = "Translator, QC, etc.", modifier = Modifier.alpha(0.35f)
                    )
                })
        }

        OutlinedTextField(enabled = mustHaveNext,
            value = localExperience.time,
            onValueChange = { value ->
                localExperience = localExperience.copy(time = value)
                experience.time = value
            },
            label = { Text("Years") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium),
            placeholder = {
                Text(
                    text = "Example: (2010 - 2020) or (2012 - Present)",
                    modifier = Modifier.alpha(0.35f)
                )
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium)
        ) {

            if (mustHaveNext) {
                Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                FloatingActionButton(onClick = {
                    onAdd(localExperience)
                    savedToast.show()
                }, content = {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                })
            } else {
                FloatingActionButton(onClick = {
                    onDelete(localExperience)
                    deletedToast.show()
                }, content = {
                    Icon(imageVector = Icons.Outlined.DeleteForever, contentDescription = null)
                })

            }
        }
        Divider(modifier = Modifier.padding(AppTheme.dimens.large))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTab(
    modifier: Modifier,
    mainUser: UserInfo,
    onSaveButtonClicked: (UserInfo) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var tempUser: UserInfo by remember { mutableStateOf(mainUser) }

    Column(
        verticalArrangement = Arrangement.Top,
    ) {
        val savedToast = Toast.makeText(LocalContext.current, "Saved", Toast.LENGTH_LONG)
        OutlinedTextField(value = tempUser.name,
            onValueChange = { value ->
                tempUser = tempUser.copy(name = value)
                mainUser.name = value
            },
            label = { Text("Full Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium)
        )

        OutlinedTextField(value = tempUser.title,
            onValueChange = { value ->
                tempUser = tempUser.copy(title = value)
                mainUser.title = value
            },
            label = { Text("Title") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium)
        )

        OutlinedTextField(value = tempUser.aboutMe,
            onValueChange = { value ->
                tempUser = tempUser.copy(aboutMe = value)
                mainUser.aboutMe = value
            },
            label = { Text("About Me") },
            maxLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium),
            placeholder = {
                Text(
                    text = "Add a brief summary about yourself.", modifier = Modifier.alpha(0.35f)
                )
            })

        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = tempUser.whatsApp,
                onValueChange = { value ->
                    tempUser = tempUser.copy(whatsApp = value)
                    mainUser.whatsApp = value
                },
                label = { Text("Phone") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .padding(AppTheme.dimens.medium)
                    .fillMaxWidth(0.45f),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone, contentDescription = null
                    )
                },
            )
            OutlinedTextField(value = tempUser.email,
                onValueChange = { value ->
                    tempUser = tempUser.copy(email = value)
                    mainUser.email = value
                },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .padding(AppTheme.dimens.medium)
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email, contentDescription = null
                    )
                })
        }
        OutlinedTextField(value = tempUser.linkedIn,
            onValueChange = { value ->
                tempUser = tempUser.copy(linkedIn = value)
                mainUser.linkedIn = value
            },
            label = { Text("Professional Profile") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium),
            placeholder = {
                Text(
                    text = "LinkedIn / ProZ.", modifier = Modifier.alpha(0.35f)
                )
            })
        Row(horizontalArrangement = Arrangement.Center) {
            Button(modifier = Modifier
                .padding(AppTheme.dimens.medium)
                .fillMaxWidth(), onClick = {
                onSaveButtonClicked(tempUser)
                savedToast.show()
            }) {
                Text(
                    text = "Save", style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

