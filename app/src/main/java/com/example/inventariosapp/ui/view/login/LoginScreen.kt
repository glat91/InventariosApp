package com.example.inventariosapp.ui.view.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.util.Constants

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val navegar = viewModel.uiState.serverValidateUser
    val state = viewModel.uiState
    val context = LocalContext.current


    LaunchedEffect(navegar){
        if (navegar){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
                popUpTo(Destinations.LoginScreen.ruta){ inclusive = true }
            }
        }
    }

    val internetUse by viewModel.baseViewModel.internetUses.collectAsState()

    LoginView(
        user = state.user,
        password = state.password,
        rememberUser = state.rememberUser,
        onClickEnter = {
            if (!viewModel.uiState.rememberUser) viewModel.clearUser()
            else viewModel.saveUserLogin()
            viewModel.validateUserLogin(
                internetUse,
                onSuccess = {
                    navController.navigate(route = Destinations.SalesScreen.ruta){
                        launchSingleTop = true
                        popUpTo(Destinations.LoginScreen.ruta){ inclusive = true }
                    }
                }
            )
        },
        onChanguerUser = { viewModel.onUserChange(it) },
        onChanguerPassword = { viewModel.onPasswordChange(it) },
        onClickRememberPassword = {
            viewModel.onRememberUserChange(!viewModel.uiState.rememberUser)
        }
    )

    Loader(viewModel.baseViewModel.getLoader())
}