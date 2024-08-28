package com.gfbdev.subtitlesage.ui.managers

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gfbdev.subtitlesage.model.Experience
import com.gfbdev.subtitlesage.model.UserInfo
import com.gfbdev.subtitlesage.pdfmanager.PdfController
import com.gfbdev.subtitlesage.ui.screens.*
import com.gfbdev.subtitlesage.R


enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.home_title), Grid(title = R.string.grid_page), Details(title = R.string.details_page), PersonalInfo(
        title = R.string.about_app
    ),
    AddTitleScreen(title = R.string.add_new_title)
}

//Top AppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: AppUiState,
    mainUser: UserInfo,
    experiences: List<Experience>,
) {
    val context = LocalContext.current
    CenterAlignedTopAppBar(title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.NavigateBefore,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            if (currentScreen.name == AppScreen.Grid.name || currentScreen.name == AppScreen.Start.name) {
                IconButton(onClick = { PdfController().generatePDF(context, uiState, mainUser, experiences) }) {
                    Icon(Icons.Filled.Share, contentDescription = "Share")
                }
            }
        })
}

//NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get name of current screen
    val currentScreen =
        AppScreen.valueOf(backStackEntry?.destination?.route ?: AppScreen.Start.name)

    val onlineViewModel: OnlineViewModel = viewModel(factory = OnlineViewModel.Factory)

    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        val userState = userViewModel.mainUser.collectAsState()
        val expState = userViewModel.mainExp.collectAsState()
        AppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() },
            uiState = onlineViewModel.uiState,
            mainUser = userState.value,
            experiences = expState.value
        )
    }) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(navController = navController, startDestination = AppScreen.Start.name) {

                composable(route = AppScreen.Start.name) {
                    val state = userViewModel.mainUser.collectAsState()
                    HomeScreen(onAboutButtonClicked = {
                        userViewModel.updateInfoBool(it)
                        navController.navigate(AppScreen.PersonalInfo.name)
                    }, onPortfolioButtonClicked = {
                        navController.navigate(AppScreen.Grid.name)
                    }, mainUser = state.value
                    )
                }

                composable(route = AppScreen.Grid.name) {
                    GridScreen(uiState = onlineViewModel.uiState,
                        retryAction = onlineViewModel::getMovieInfo,
                        onDetailsButtonClicked = {
                            onlineViewModel.updateCurrentMovie(it)
                            navController.navigate(AppScreen.Details.name)
                        },
                        onAddButtonClicked = {
                            navController.navigate(AppScreen.AddTitleScreen.name)
                        },
                        onDeleteButtonClicked = {
                            onlineViewModel.deleteTitle(it)
                        })
                }

                composable(route = AppScreen.Details.name) {
                    val state = onlineViewModel.currentMovie.collectAsState()
                    DetailScreen(movieInfoLocal = state.value)
                }

                composable(route = AppScreen.PersonalInfo.name) {
                    val userState = userViewModel.mainUser.collectAsState()
                    val expState = userViewModel.mainExp.collectAsState()
                    val infoBool = userViewModel.personalInfoBool.collectAsState()
                    PersonalInfoScreen(
                        infoBool = infoBool.value,
                        mainUser = userState.value,
                        experiences = expState.value,
                        onSaveButtonClicked = { userViewModel.saveMainUser(it) },
                        onAddExperience = { userViewModel.addExp(it) },
                        onDeleteExperience = { userViewModel.deleteExp(it) })
                }

                composable(route = AppScreen.AddTitleScreen.name) {
                    val state = onlineViewModel.addTitleCurrentMovie.collectAsState()
                    AddTitleScreen(addTitleCurrentMovie = state.value, onSearchButtonClicked = {
                        onlineViewModel.getTitleToAdd(it)
                    }) {
                        onlineViewModel.addTitle()
                    }
                }
            }
        }
    }
}