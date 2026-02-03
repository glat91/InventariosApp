package com.example.inventariosapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.navigation.SetupNavGraph
import com.example.inventariosapp.ui.component.ButtonCmp
import com.example.inventariosapp.ui.dialog.BasicDialogCmp
import com.example.inventariosapp.ui.theme.InventariosAppTheme
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.view.menu.LateralMenuCmp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object{
        lateinit var drawerState: DrawerState
        lateinit var scope: CoroutineScope
        // region Vars Menu
        var lastUpdateClient: MutableState<String> = mutableStateOf("")
        var lastUpdateProducts: MutableState<String> = mutableStateOf("")
        var lastUpdateSells: MutableState<String> = mutableStateOf("")
        var lastUpdatePayments: MutableState<String> = mutableStateOf("")
        var lastUpdateInventory: MutableState<String> = mutableStateOf("")
        val internetBtn: MutableState<Boolean> = mutableStateOf(true)
        // endregion
        // region Vars Main Dialog
        val mainDialog = mutableStateOf(false)
        var mainDialogTitle = mutableStateOf("Aviso")
        var mainDialogMsg = mutableStateOf("")
        var mainDialogColor = Color.Red
        // endregion
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            InventariosAppTheme() {
                val navController = rememberNavController()
                scope = rememberCoroutineScope()
                SetupNavGraph(navController)
                if (mainDialog.value){
                    BasicDialogCmp(
                        color = mainDialogColor,
                        content = {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextCmp(
                                    text = mainDialogTitle.value,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 32.dp, bottom = PADDING_16)
                                )

                                TextCmp(
                                    text = mainDialogMsg.value,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = PADDING_16)
                                )

                                ButtonCmp(
                                    onClick = { mainDialog.value = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(50),
                                    backGroundColor = Color.Green,
                                    textSize = 14.sp,
                                    text = "Aceptar"
                                )
                            }
                        },
                        onDismiss = { mainDialog.value = false }
                    )
                }

                LateralMenuCmp(
                    navController = navController,
                    drawerState = drawerState,
                    screenContent = { SetupNavGraph(navController) }
                )
            }
        }
    }
}

