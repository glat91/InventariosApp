package com.example.inventariosapp.ui.view.menu


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.R
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.util.Constants
import kotlinx.coroutines.launch
@Composable
fun LateralMenuCmp(
    drawerState: DrawerState,
    navController: NavHostController,
    screenContent: @Composable () -> Unit
) {
    // region Vars
    val coroutine = rememberCoroutineScope()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val cnx = LocalContext.current
    val version = cnx.packageManager.getPackageInfo(cnx.packageName, 0).versionName

    LaunchedEffect(true) {
        if (menuViewModel.userName.value.isBlank()) {
            menuViewModel.userName.value = menuViewModel.baseViewModel.getGetName()
        }
    }
    // endregion
    // region View
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.paint(
                    painter = painterResource(id = R.drawable.metal2_bg),
                    contentScale = ContentScale.None
                ))
            {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    DrawerHeader(
                        userName = menuViewModel.userName.value,
                        version = version ?: ""
                    )
                    // region Opciones Nav
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        // Navegación
                        DrawerSectionLabel("Menú")
                        DrawerNavItem(
                            icon = Icons.Outlined.BarChart,
                            label = "Ventas",
                            selected = MainActivity.currentRoute.value == Destinations.SalesScreen.ruta
                        ) {
                            navController.navigate(Destinations.SalesScreen.ruta) { launchSingleTop = true }
                            coroutine.launch { drawerState.close() }
                        }
                        DrawerNavItem(
                            icon = Icons.Outlined.Payments,
                            label = "Pagos",
                            selected = MainActivity.currentRoute.value == Destinations.PaymentScreen.ruta
                        ) {
                            navController.navigate(Destinations.PaymentScreen.ruta) { launchSingleTop = true }
                            coroutine.launch { drawerState.close() }
                        }
                        if (menuViewModel.baseViewModel.getUsarId() == 1) {
                            DrawerNavItem(
                                icon = Icons.Outlined.Inventory2,
                                label = "Productos",
                                selected = MainActivity.currentRoute.value == Destinations.ProductsScreen.ruta
                            ) {
                                navController.navigate(Destinations.ProductsScreen.ruta) { launchSingleTop = true }
                                coroutine.launch { drawerState.close() }
                            }
                        }
                        DrawerNavItem(
                            icon = Icons.Outlined.People,
                            label = "Ventas pendientes",
                            selected = MainActivity.currentRoute.value == Destinations.PenndingSaleScreen.ruta
                        ) {
                            navController.navigate(Destinations.PenndingSaleScreen.ruta) { launchSingleTop = true }
                            coroutine.launch { drawerState.close() }
                        }
                        DrawerNavItem(
                            icon = Icons.Outlined.HourglassTop,
                            label = "Pagos pendientes",
                            selected = MainActivity.currentRoute.value == Destinations.PenndingPaymentsScreen.ruta
                        ) {
                            navController.navigate(Destinations.PenndingPaymentsScreen.ruta) { launchSingleTop = true }
                            coroutine.launch { drawerState.close() }
                        }

                        DrawerDivider()

                        // Sincronización
                        DrawerSectionLabel("Sincronización")
                        DrawerSyncItem("Clientes",   MainActivity.lastUpdateClient.value)   { menuViewModel.updateClientsDb() }
                        DrawerSyncItem("Productos",  MainActivity.lastUpdateProducts.value)  { menuViewModel.updateProductsDb() }
                        DrawerSyncItem("Ventas",     MainActivity.lastUpdateSells.value)     { menuViewModel.updatePendingSales() }
                        DrawerSyncItem("Inventario", MainActivity.lastUpdateInventory.value) { menuViewModel.updateInventory() }

                        DrawerDivider()

                        // Opciones
                        DrawerSectionLabel("Opciones")
                        DrawerToggleItem(
                            isOnline = MainActivity.internetBtn.value,
                            onToggle = {
                                MainActivity.internetBtn.value = !MainActivity.internetBtn.value
                                menuViewModel.saveBoolean(cnx, Constants.INTERNET, MainActivity.internetBtn.value)
                            }
                        )
                    }

                    // ── Footer — Cerrar sesión ────────────────────────
                    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                menuViewModel.baseViewModel.closeMenu()
                                menuViewModel.baseViewModel.logoutNoMsj()
                                MainActivity.mainDialogMsg.value = "Datos de sesión borrados"
                                MainActivity.mainDialog.value = true
                                navController.navigate(Destinations.SalesScreen.ruta) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(28.dp).clip(RoundedCornerShape(7.dp))
                                .background(Color(0xFFE24B4A).copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Close, contentDescription = null, tint = Color(0xFFE24B4A), modifier = Modifier.size(16.dp))
                        }
                        TextCmp("Cerrar sesión", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE24B4A))
                    }
                    // endregion
                }
            }
        },
        drawerState = drawerState
    ) {
        screenContent()
    }
    // endregion
    Loader(menuViewModel.baseViewModel.getLoader())
}

// ── Sub-componentes ──────────────────────────────────────────────────────────

@Composable
private fun DrawerHeader(userName: String, version: String) {
    val initials = userName.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center
        ) {
            TextCmp(initials.ifEmpty { "U" }, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            TextCmp("Hola, $userName", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A), maxLine = 1, overflow = TextOverflow.Ellipsis)
            TextCmp("v$version", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color.Black.copy(0.38f),
                modifier = Modifier.clip(RoundedCornerShape(5.dp)).background(Color(0xFFF3F3F3)).padding(horizontal = 6.dp, vertical = 2.dp))
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
}

@Composable
private fun DrawerSectionLabel(text: String) {
    TextCmp(
        text = text.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.em,
        color = Color.Black.copy(alpha = 0.3f),
        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 4.dp)
    )
}

@Composable
private fun DrawerNavItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) Color(0xFF1A1A1A) else Color.Transparent, tween(180), label = "navBg")
    val contentColor = if (selected) Color.White else Color(0xFF1A1A1A)
    val iconBg = if (selected) Color.White.copy(0.15f) else Color(0xFFF3F3F3)
    val iconTint = if (selected) Color.White else Color.Black.copy(0.55f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(iconBg), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        TextCmp(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = contentColor)
    }
}

@Composable
private fun DrawerSyncItem(label: String, date: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(7.dp)).background(Color(0xFFF3F3F3)), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Refresh, contentDescription = null, tint = Color.Black.copy(0.4f), modifier = Modifier.size(15.dp))
        }
        TextCmp(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        TextCmp(
            text = if (date.isBlank()) "Sin actualizar" else date,
            fontSize = 9.sp,
            color = Color.Black.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun DrawerToggleItem(isOnline: Boolean, onToggle: () -> Unit) {
    val thumbOffset by animateDpAsState(if (isOnline) 18.dp else 2.dp, spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "thumb")
    val trackColor by animateColorAsState(if (isOnline) Color(0xFF1A1A1A) else Color(0xFFD5D5D5), tween(200), label = "track")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onToggle)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(7.dp)).background(Color(0xFFF3F3F3)), contentAlignment = Alignment.Center) {
            Icon(if (isOnline) Icons.Outlined.Wifi else Icons.Outlined.WifiOff, contentDescription = null, tint = Color.Black.copy(0.4f), modifier = Modifier.size(15.dp))
        }
        TextCmp(if (isOnline) "Modo Online" else "Modo Offline", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        // Switch custom
        Box(
            modifier = Modifier
                .width(36.dp).height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = thumbOffset.coerceAtLeast(0.dp), top = 2.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun DrawerDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
}