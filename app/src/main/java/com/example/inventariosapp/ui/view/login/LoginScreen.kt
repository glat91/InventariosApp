package com.example.inventariosapp.ui.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.InventariosAppTheme

@Composable
fun LoginScreen() {
    Box(modifier = Modifier.background(Color.Red).fillMaxSize()){
        TextCmp("Algo")
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(){
    LoginScreen()
}