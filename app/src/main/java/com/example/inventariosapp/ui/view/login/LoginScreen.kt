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
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val navegar = viewModel.serverValidateUser.collectAsState()


    val context = LocalContext.current
    LaunchedEffect(navegar.value){
        if (navegar.value){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
                popUpTo(Destinations.LoginScreen.ruta){ inclusive = true }
            }
        }
    }

    val internetUse by viewModel.baseViewModel.internetUses.collectAsState()


    LoginView(
        user = viewModel.user,
        password = viewModel.password,
        rememberUser = viewModel.rememberUser,
        onClickEnter = {
            if (!viewModel.rememberUser.value) viewModel.clearUser(context)
            else viewModel.saveUserLogin(context)
            viewModel.validateUserLogin(internetUse)
        },
        onClickRememberPassword = {
            viewModel.rememberUser.value = !viewModel.rememberUser.value
        }
    )

    Loader(viewModel.baseViewModel.getLoader())
}