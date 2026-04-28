package com.example.inventariosapp.ui.view.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader

@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(uiState.serverValidateUser) {
        if (uiState.serverValidateUser) {
            navController.navigate(route = Destinations.SalesScreen.ruta) {
                launchSingleTop = true
                popUpTo(Destinations.LoginScreen.ruta) { inclusive = true }
            }
        }
    }

    val internetUse by viewModel.baseViewModel.internetUses.collectAsState()

    LoginView(
        uiState = uiState,
        onClickEnter = {
            if (!uiState.rememberUser) viewModel.clearUser()
            else viewModel.saveUserLogin()
            viewModel.validateUserLogin(internetUse)
        },
        onUserChange = { viewModel.updateUser(it) },
        onPasswordChange = { viewModel.updatePassword(it) },
        onClickRememberPassword = { viewModel.toggleRememberUser() }
    )

    Loader(viewModel.baseViewModel.getLoader())
}