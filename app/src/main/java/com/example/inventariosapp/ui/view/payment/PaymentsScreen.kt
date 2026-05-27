package com.example.inventariosapp.ui.view.payment

import android.Manifest
import android.bluetooth.BluetoothClass
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.component.ButtonCmp
import com.example.inventariosapp.ui.component.ButtonWithImgCmp
import com.example.inventariosapp.ui.component.cards.CardDepositCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.dialog.BasicDialogCmp
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_BACKGROUND_BT
import com.example.inventariosapp.ui.theme.UI_BT
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Accept
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Cancel
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Green
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Yellow
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.view.login.LoginViewModel
import com.example.inventariosapp.util.Helpers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(navController: NavHostController) {
    val viewModel: PaymentsViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current

    @OptIn(ExperimentalMaterial3Api::class)
    var datePickerState = rememberDatePickerState()

    val uiState by viewModel.uiState.collectAsState()
    val luiState by lviewModel.uiState.collectAsState()

    PaymentsView(
        data = viewModel.filterPayments(),
        dateStart = uiState.startDate,
        dateEnd = uiState.endDate,
        search = uiState.search,
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickDate = { viewModel.updateShowDatePicker(true) },
        onClickRow = {
            viewModel.updateSelect(it)
            viewModel.getPayment(it.folio.toString())
        },
        onChangueSearchBarTxt = { viewModel.updateSearch(it) },
        changueDialogChoice = {
            viewModel.updateDialogChoice(it)
        }
    )

    // region BT
    LaunchedEffect(Unit) {
        //viewModel.hasPermissions = viewModel.hasPermissions(cnx)
    }
    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        viewModel.updateHasPermissions(result.values.all { it })
        if (uiState.hasPermissions) { viewModel.updateDialogBT(true) }
    }
    if (uiState.dialogBT && uiState.hasPermissions) {
        BasicDialogCmp(
            color = UI_BT,
            content = {
                Column(modifier = Modifier) {
                    if (!uiState.hasPermissions) {
                        Text("Se necesitan permisos Bluetooth", modifier = Modifier)
                        return@Column
                    }

                    val isEnabled = uiState.bluetoothAdapter.isEnabled
                    if (!isEnabled) {
                        Text("Activa el Bluetooth e intenta de nuevo")
                        return@Column
                    }

                    // Cargar dispositivos emparejados
                    LaunchedEffect(Unit) @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT) {
                        uiState.bondedDevices.clear()
                        uiState.bluetoothAdapter.bondedDevices?.forEach { device ->

                            val hasPrinterUUID = device.uuids?.any {
                                it.uuid == uiState.printerUUID
                            } == true

                            val isImagingDevice =
                                device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING

                            if (hasPrinterUUID || isImagingDevice) {
                                uiState.bondedDevices.add(device)
                            }
                        }
                    }

                    if (uiState.bondedDevices.isEmpty()) {
                        Text("No hay dispositivos emparejados")
                    } else {
                        Text(
                            modifier = Modifier.padding(PADDING_16),
                            text = "Selecciona el dispositivo Bluetooth"
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            items(uiState.bondedDevices) { device ->
                                Row(
                                    modifier = Modifier
                                        .padding(PADDING_4)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(UI_BACKGROUND_BT)
                                        .clickable {
                                            if (uiState.payTotalPayment.isEmpty()){
                                                Log.i("print___", "printy")
                                                scope.launch {
                                                    viewModel.connectAndReprint(
                                                        context = cnx,
                                                        device = device,
                                                    )
                                                }
                                                viewModel.cleanDialog()
                                            }
                                            else{
                                                Log.i("print___", "Reprinty")
                                                scope.launch {
                                                    viewModel.connectAndPrint(
                                                        context = cnx,
                                                        device = device,
                                                    )
                                                }
                                            }
                                        },
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,

                                    ) {
                                    VerticalDivider(color = Color.Gray, thickness = PADDING_4)
                                    Text(
                                        color = Color.White,
                                        text = device.name ?: "No name"
                                    )
                                    VerticalDivider(color = Color.Gray, thickness = PADDING_4)
                                }
                            }
                        }
                    }
                }
            },
            onDismiss = { viewModel.updateDialogBT(false) }
        )
    }
    // endregion
    // region Dialog Date
    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.updateShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateDateInput(datePickerState)
                    viewModel.updateShowDatePicker(false)
                    viewModel.getPendingSales()
                }) {
                    TextCmp("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.updateShowDatePicker(false) }) {
                    TextCmp("Cancelar")
                }
            }
        ) { DatePicker(state = datePickerState) }
    }
    // endregion
    // region Dialog Deposit
    if (uiState.dialogDeposit) {
        BasicDialogCmp(
            color = UI_Backround_Top,
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = PADDING_16)
                            .border(
                                2.dp,
                                Color.Gray,
                                RoundedCornerShape(10.dp)
                            )
                        ,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 4.dp,
                            hoveredElevation = 0.dp,
                            draggedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(0.5.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        ) {
                            TotalRow(
                                label = "TOTAL",
                                value = "$${uiState.select?.total}"
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
                            TotalRow(
                                label = "ADEUDO",
                                value = "$${uiState.select?.montoPorPagar}",
                                valueColor = Color(0xFFE24B4A)
                            )
                        }
                        Card(
                            modifier = Modifier.padding(PADDING_4),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 20.dp,
                                pressedElevation = 0.dp,
                                focusedElevation = 15.dp,
                                hoveredElevation = 0.dp,
                                draggedElevation = 0.dp,
                                disabledElevation = 0.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = UI_Backround_Btn_Green,
                                contentColor = Color.White
                            )
                        ){ }
                    }
                    HorizontalDivider(thickness = PADDING_16, color = Color.Transparent)
                    if (uiState.showDeposit) {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { -it })
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ButtonWithImgCmp(
                                    text = "Agregar pago",
                                    backgroundColor = UI_Backround_Btn_Yellow,
                                    icon = Icons.Filled.Add,
                                    onClick = { viewModel.updateShowDeposit(false) }
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = PADDING_16, color = Color.Transparent)
                    TextCmp(
                        text = "Pagos",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        maxLine = 1
                    )
                    HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                    HorizontalDivider(thickness = 2.dp, color = Color.Black)
                    if (uiState.showDeposit) {
                        AnimatedVisibility(
                            visible = uiState.showDeposit,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { -it })
                        ) {
                            Column {
                                HorizontalDivider(thickness = 1.dp, color = UI_Divier)
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(uiState.payments) { deposit ->
                                        CardDepositCmp(
                                            date = deposit.fecha.toString(),
                                            totalAmount = deposit.montoPago.toString(),
                                            observations = deposit.observaciones.toString(),
                                            onClickDelete = {
                                                viewModel.deletePayment(deposit.ventaPagoId!!, deposit, cnx)
                                            },
                                            onClickPrint = {
                                                Log.i("Print___", deposit.toString())
                                                permissionLauncher.launch(viewModel.permissions)
                                            }
                                        )
                                        HorizontalDivider(thickness = PADDING_4, color = Color.Transparent)
                                    }
                                }
                            }

                        }
                    }
                    else {
                        AnimatedVisibility(
                            visible = !uiState.showDeposit,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { -it })
                        ) {

                            Column {
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Fecha",
                                    keyboardType = KeyboardType.Number,
                                    textValue = uiState.payActualDate,
                                    onValueChange = { },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    readOnly = true,
                                )
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Importe",
                                    keyboardType = KeyboardType.Number,
                                    textValue = uiState.payTotalPayment,
                                    onValueChange = { viewModel.updatePayTotalPayment(it) },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    enabled = true,
                                )
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Observaciones",
                                    keyboardType = KeyboardType.Text,
                                    textValue = uiState.payObservation,
                                    onValueChange = { if (it.length <= 50) viewModel.updatePayObservation(it) },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    enabled = true,
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = PADDING_16),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    ButtonCmp(
                                        modifier = Modifier,
                                        text = "Cancelar",
                                        onClick = { viewModel.updateShowDeposit(true) },
                                        enable = true,
                                        shape = RoundedCornerShape(10.dp),
                                        txtColor = Color.White,
                                        maxLines = 1,
                                        backGroundColor = UI_Backround_Btn_Cancel,
                                        disableBackGroundColor = Color.Gray,
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 0.dp)
                                    )

                                    ButtonCmp(
                                        modifier = Modifier,
                                        text = "Agregar",
                                        onClick = {
                                            val monto = uiState.payTotalPayment.toDoubleOrNull() ?: 0.0
                                            if (monto > 0.01 && monto <= uiState.select?.montoPorPagar!!) {
                                                viewModel.setPayment(
                                                    ventaId = uiState.select!!.ventaId!!,
                                                    montoPago = monto,
                                                    observaciones = uiState.payObservation,
                                                    onSuccess = {
                                                        permissionLauncher.launch(viewModel.permissions)
                                                    }
                                                )
                                                viewModel.updateShowDeposit(true)
                                            } else {
                                                if (monto > uiState.select?.montoPorPagar!!) {
                                                    MainActivity.mainDialogMsg.value = "El monto debe ser menor al adeudo"
                                                } else {
                                                    MainActivity.mainDialogMsg.value = "El monto debe ser mayor a .01 centavo"
                                                }
                                                viewModel.cleanPayment()
                                                MainActivity.mainDialog.value = true
                                            }
                                        },
                                        enable = !uiState.payTotalPayment.isEmpty(),
                                        shape = RoundedCornerShape(10.dp),
                                        txtColor = Color.White,
                                        maxLines = 1,
                                        backGroundColor = UI_Backround_Btn_Accept,
                                        disableBackGroundColor = Color.Gray,
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 0.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            onDismiss = { if (!viewModel.baseViewModel.dialogLogin.value) viewModel.cleanDialog() }
        )
    }
    // endregion
    // region Dialog Login
    if (viewModel.baseViewModel.dialogLogin.value) {
        LoginDialogCmp(
            uiState = luiState,
            onClickEnter = {
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                if (!luiState.rememberUser) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(internetUse)
            },
            onUserChange = { lviewModel.updateUser(it) },
            onPasswordChange = { lviewModel.updatePassword(it) },
            onClickRememberPassword = { lviewModel.toggleRememberUser() }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}


@Composable
private fun TotalRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFF1A1A1A)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.07.em,
            color = Color.Black.copy(alpha = 0.38f)
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            maxLines = 1
        )
    }
}