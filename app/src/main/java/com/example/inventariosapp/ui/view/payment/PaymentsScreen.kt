package com.example.inventariosapp.ui.view.payment

import android.bluetooth.BluetoothClass
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import com.example.inventariosapp.ui.view.login.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(navController: NavHostController) {
    val viewModel: PaymentsViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current

    @OptIn(ExperimentalMaterial3Api::class)
    var datePickerState = rememberDatePickerState()

    val showDeposit = remember { mutableStateOf(true) }

    PaymentsView(
        data = viewModel.sales.value,
        dateStart = viewModel.startDate.value,
        dateEnd = viewModel.endDate.value,
        clickDate = viewModel.dialogChoice,
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickAdd = { viewModel.dialogDeposit.value = true},
        onClickDate = { viewModel.showDatePicker.value = true },
        onClickRow = {
            viewModel.select.value = it
            viewModel.getPayment(it.folio.toString())
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
        viewModel.hasPermissions = result.values.all { it }
        if (viewModel.hasPermissions) {
            viewModel.dialogBT = true
        }
    }
    if (viewModel.dialogBT && viewModel.hasPermissions){
        BasicDialogCmp(
            color = UI_BT,
            content = {
                Column(modifier = Modifier) {
                    if (!viewModel.hasPermissions) {
                        Text("Se necesitan permisos Bluetooth", modifier = Modifier)
                        return@Column
                    }

                    val isEnabled = viewModel.bluetoothAdapter?.isEnabled == true
                    if (!isEnabled) {
                        Text("Activa el Bluetooth e intenta de nuevo")
                        return@Column
                    }

                    // Cargar dispositivos emparejados
                    LaunchedEffect(Unit) {
                        viewModel.bondedDevices.clear()
                        viewModel.bluetoothAdapter.bondedDevices?.forEach { device ->

                            val hasPrinterUUID = device.uuids?.any {
                                it.uuid == viewModel.printerUUID
                            } == true

                            val isImagingDevice =
                                device.bluetoothClass?.majorDeviceClass ==
                                        BluetoothClass.Device.Major.IMAGING

                            if (hasPrinterUUID || isImagingDevice) {
                                viewModel.bondedDevices.add(device)
                            }
                        }
                    }

                    if (viewModel.bondedDevices.isEmpty()) {
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
                            items(viewModel.bondedDevices) { device ->
                                Log.i("Items___", device.toString())
                                Row(
                                    modifier = Modifier
                                        .padding(PADDING_4)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(UI_BACKGROUND_BT)
                                        .clickable{
                                            scope.launch {
                                                viewModel.connectAndPrint(
                                                    context = cnx,
                                                    device = device,
                                                )
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
            onDismiss = { viewModel.dialogBT = false}
        )
    }
    // endregion
    // region Dialog Date
    if (viewModel.showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { viewModel.showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateDateInput(datePickerState)
                    viewModel.showDatePicker.value = false
                    viewModel.getPendingSales()
                }) {
                    TextCmp("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDatePicker.value = false }) {
                    TextCmp("Cancelar")
                }
            }
        ) { DatePicker(state = datePickerState) }
    }
    // endregion
    // region Dialog Deposit
    if (viewModel.dialogDeposit.value){
        BasicDialogCmp(
            color = UI_Backround_Top,
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = PADDING_16)
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
                            modifier = Modifier.padding(PADDING_4),
                            verticalArrangement = Arrangement.Center,
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                TextCmp(
                                    text = "Total:",
                                    modifier = Modifier.padding(PADDING_4),
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily.Serif,
                                    textDecoration = TextDecoration.None,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Visible,
                                    maxLine = 1
                                )
                                TextCmp(
                                    text = "${viewModel.select.value?.total}",
                                    modifier = Modifier.padding(PADDING_4).fillMaxWidth(),
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily.Serif,
                                    textDecoration = TextDecoration.None,
                                    textAlign = TextAlign.Right,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Visible,
                                    maxLine = 1
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                TextCmp(
                                    text = "Adeudo:",
                                    modifier = Modifier.padding(PADDING_4),
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily.Serif,
                                    textDecoration = TextDecoration.None,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Visible,
                                    maxLine = 1
                                )
                                TextCmp(
                                    text = "${viewModel.select.value?.montoPorPagar}",
                                    modifier = Modifier.padding(PADDING_4).fillMaxWidth(),
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = FontFamily.Serif,
                                    textDecoration = TextDecoration.None,
                                    textAlign = TextAlign.Right,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Visible,
                                    maxLine = 1
                                )
                            }
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
                        ){}
                    }
                    HorizontalDivider(thickness = PADDING_16, color = Color.Transparent)
                    if (showDeposit.value){
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = {-it})
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ButtonWithImgCmp(
                                    text = "Agregar pago",
                                    backGroundColor = UI_Backround_Btn_Yellow,
                                    icon = Icons.Filled.Add,
                                    onClick = { showDeposit.value = false }
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
                    if (showDeposit.value){
                        AnimatedVisibility(
                            visible = showDeposit.value,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = {-it})
                        ){
                            Column {
                                HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    var switchColor = true
                                    items(viewModel.payments.value) { deposit ->
                                        var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                                        CardDepositCmp(
                                            date = deposit.fecha.toString(),
                                            totalAmount = deposit.montoPago.toString(),
                                            observations = deposit.observaciones.toString(),
                                            backgroundColor = colorRow,
                                            onClickDelete = {
                                                viewModel.deletePayment(deposit.ventaPagoId!!, deposit, cnx)
                                            },
                                            onClickPrint = {
                                                permissionLauncher.launch(viewModel.permissions)
                                            }
                                        )
                                        HorizontalDivider(thickness = PADDING_4, color = Color.Transparent, )
                                        HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                                        switchColor = !switchColor
                                    }
                                }
                            }

                        }
                    }
                    else{
                        AnimatedVisibility(
                            visible = !showDeposit.value,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = {-it})
                        ){

                            Column {
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Fecha",
                                    keyboardType = KeyboardType.Number,
                                    textValue = viewModel.payActualDate,
                                    onValueChange = {  },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    readOnly = true,
                                )
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Importe",
                                    keyboardType = KeyboardType.Number,
                                    textValue = viewModel.payTotalPayment.value,
                                    onValueChange = { viewModel.payTotalPayment.value = it },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    enabled = true,
                                )
                                InputWithTitleLabelCmp(
                                    modifier = Modifier,
                                    labelText = "Observaciones",
                                    keyboardType = KeyboardType.Text,
                                    textValue = viewModel.payObservation.value,
                                    onValueChange = { if (it.length <= 50) viewModel.payObservation.value = it },
                                    textAlign = TextAlign.Left,
                                    disableTextColor = Color.Gray,
                                    enabled = true,
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = PADDING_16),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    ButtonCmp(
                                        modifier = Modifier,
                                        text = "Cancelar",
                                        onClick = { showDeposit.value = true },
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
                                            val monto = viewModel.payTotalPayment.value.toDoubleOrNull() ?: 0.0
                                            if (monto > 0.01 && monto <= viewModel.select.value?.montoPorPagar!!){
                                                viewModel.setPayment(
                                                    ventaId = viewModel.select.value!!.ventaId!!,
                                                    montoPago = monto,
                                                    observaciones = viewModel.payObservation.value,
                                                    onSuccess = {
                                                        permissionLauncher.launch(viewModel.permissions)
                                                    }
                                                )
                                                showDeposit.value = true
                                            }
                                            else {
                                                if (monto > viewModel.select.value?.montoPorPagar!!){
                                                    MainActivity.mainDialogMsg.value = "El monto debe ser menor al adeudo"
                                                }
                                                else{
                                                    MainActivity.mainDialogMsg.value = "El monto debe ser mayor a .01 centavo"
                                                }
                                                viewModel.cleanPayment()
                                                MainActivity.mainDialog .value = true
                                            }
                                        },
                                        enable = !viewModel.payTotalPayment.value.isEmpty(),
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
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            user = lviewModel.user,
            password = lviewModel.password,
            rememberUser = remember { mutableStateOf(false) },
            onClickEnter = {
                if (!lviewModel.rememberUser.value) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(MainActivity.internetBtn.value)
            },
            onClickRememberPassword = {
                lviewModel.rememberUser.value = !lviewModel.rememberUser.value
            }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}
