package com.example.inventariosapp.ui.view.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.R
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import kotlinx.coroutines.launch
@Composable
fun LateralMenuCmp(
    drawerState: DrawerState,
    navController: NavHostController,
    screenContent: @Composable () -> Unit
) {
    val corutine = rememberCoroutineScope()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val cnx = LocalContext.current

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.paint(
                    painter = painterResource(id = R.drawable.metal2_bg),
                    contentScale = ContentScale.None
                ))
            {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    // region Opciones Nav
                    TextCmp(
                        text = "Menu",
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 20.sp
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_bar_chart),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        label = {
                            TextCmp(
                                text = "Ventas",
                                fontSize = 14.sp
                            ) },
                        selected = false,
                        onClick = {
                            navController.navigate(route = Destinations.SalesScreen.ruta){
                                launchSingleTop = true
                            }
                            corutine.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_money),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        label = {
                            TextCmp(
                                text = "Pagos",
                                fontSize = 14.sp
                            ) },
                        selected = false,
                        onClick = {
                            navController.navigate(route = Destinations.PaymentScreen.ruta){
                                launchSingleTop = true
                            }
                            corutine.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cases),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        label = {
                            TextCmp(
                                text = "Productos",
                                fontSize = 14.sp
                            ) },
                        selected = false,
                        onClick = {
                            navController.navigate(route = Destinations.Inventory.ruta){
                                launchSingleTop = true
                            }
                            corutine.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pennding_sale),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        label = {
                            TextCmp(
                                text = "Ventas pendientes",
                                fontSize = 14.sp
                            ) },
                        selected = false,
                        onClick = {
                            navController.navigate(route = Destinations.PenndingSale.ruta){
                                launchSingleTop = true
                            }
                            corutine.launch { drawerState.close() }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    // endregion
                    // region Update Data
                    TextCmp("Sincronizacion")
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Clientes",
                                fontSize = 14.sp,
                                maxLine = 2
                            ) },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        badge = {
                            val date = MainActivity.lastUpdateClient.value
                            TextCmp(
                                text = if (date.isBlank()) "No update" else "Update ${date}",
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { menuViewModel.updateClientsDb() }
                    )
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Productor",
                                fontSize = 14.sp,
                                maxLine = 2
                            )
                        },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        badge = {
                            val date = MainActivity.lastUpdateProducts.value
                            TextCmp(
                                text = if (date.isBlank()) "No update" else "Update ${date}",
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { menuViewModel.updateProductsDb() },
                    )
                    /* TODO Borrar Cambio implementacion Inventario es igual que Products
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Inventario",
                                fontSize = 14.sp,
                                maxLine = 2
                            )
                        },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        badge = {
                            val date = MainActivity.lastUpdateInventory.value
                            TextCmp(
                                text = if (date.isBlank()) "No update" else "Update ${date}",
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { menuViewModel.updateInventory() },
                    )
                     */
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Ventas",
                                fontSize = 14.sp,
                                maxLine = 2
                            )
                        },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        badge = {
                            val date = MainActivity.lastUpdateSells.value

                            TextCmp(
                                text = if (date.isBlank()) "No update" else "Update ${date}",
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { menuViewModel.updatePendingSales() },
                    )
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Pagos",
                                fontSize = 14.sp,
                                maxLine = 2
                            )
                        },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        badge = {
                            val date = MainActivity.lastUpdatePayments.value
                            TextCmp(
                                text = if (date.isBlank()) "No update" else "Update ${date}",
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { menuViewModel.updatePayment() },
                    )
                    Spacer(Modifier.height(12.dp))
                    // endregion
                    // region Internet
                    TextCmp("Opciones")
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = if(MainActivity.internetBtn.value)"Modo Online" else "Modo Offline",
                                fontSize = 14.sp,
                                maxLine = 2
                            ) },
                        selected = false,
                        badge = {
                            RadioButton(
                                selected = MainActivity.internetBtn.value,
                                onClick = {
                                    MainActivity.internetBtn.value = !MainActivity.internetBtn.value
                                    menuViewModel.saveBoolean(
                                        cnx,
                                        Constants.INTERNET,
                                        MainActivity.internetBtn.value
                                    )
                                }
                            )
                        },
                        icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                        onClick = { menuViewModel.updateClientsDb() }
                    )
                    // endregion
                    // region Session
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            TextCmp(
                                text = "Cerrar Session",
                                fontSize = 14.sp
                            )
                        },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Close, contentDescription = null) },
                        onClick = {
                            menuViewModel.baseViewModel.closeMenu()
                            navController.navigate(Destinations.LoginScreen.ruta){
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                    )
                    // endregion
                }
            }
        },
        drawerState = drawerState
    ) {
        screenContent()

    }
    Loader(menuViewModel.baseViewModel.getLoader())
}