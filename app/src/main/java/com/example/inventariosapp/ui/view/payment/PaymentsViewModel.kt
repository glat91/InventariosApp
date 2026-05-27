package com.example.inventariosapp.ui.view.payment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.R
import com.example.inventariosapp.domain.use_case.client.GetClientsUseCase
import com.example.inventariosapp.domain.use_case.payment.DeletePaymentUseCase
import com.example.inventariosapp.domain.use_case.payment.GetPaymentUseCase
import com.example.inventariosapp.domain.use_case.payment.PostPaymentUseCase
import com.example.inventariosapp.domain.use_case.sales.GetPendingSalesUseCase
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.payment.PayModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.ui.view.BluetoothPrinterScreen.printBitmap
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Method
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.update
import java.time.format.DateTimeFormatter

fun String.toMexicanDate(): String {
    return try {
        LocalDate.parse(this)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        this
    }
}
data class PaymentsUiState(
    val search: TextFieldValue = TextFieldValue(""),
    val selectedDate: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
    val dialogChoice: Boolean = false,
    val clients: List<ClientResponseModel> = listOf(),
    val filterData: List<SalesModel> = arrayListOf(),
    val select: SalesModel? = null,
    val sales: List<SalesModel> = arrayListOf(),
    val payActualDate: String = Helpers.getDate(),
    val payTotalPayment: String = "",
    val payObservation: String = "",
    val dialogDeposit: Boolean = false,
    val showDeposit: Boolean = false,
    val payments: ArrayList<PayModel> = arrayListOf(),
    val dialogBT: Boolean = false,
    val hasPermissions: Boolean = false,
    val permissions: List<String> = emptyList(),
    val printerUUID: UUID = UUID.fromString(Constants.PRINTER_UUID),
    val bluetoothAdapter: BluetoothAdapter =  BluetoothAdapter.getDefaultAdapter(),
    val bondedDevices: ArrayList<BluetoothDevice> =  arrayListOf<BluetoothDevice>(),
)

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsUseCase,
    private val getPendingSalesUseCase: GetPendingSalesUseCase,
    private val getPaymentUseCase: GetPaymentUseCase,
    private val postPaymentUseCase: PostPaymentUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx: Context
) : ViewModel() {
    // region UiState
    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState = _uiState.asStateFlow()
    // endregion
    // region init
    init {
        getPendingSales()
        _uiState.value = _uiState.value.copy(
            hasPermissions = ContextCompat.checkSelfPermission(
                cnx,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        cnx,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissions = buildList {
        add(Manifest.permission.BLUETOOTH_CONNECT)
        add(Manifest.permission.BLUETOOTH_SCAN)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }.toTypedArray()
    // endregion
    // region Update uiState
    fun updateSearch(query: TextFieldValue) {
        _uiState.update { it.copy(search = query) }
    }
    fun cleanPayment() {
        _uiState.update { it.copy(payTotalPayment = "", payObservation = "") }
    }
    fun updateHasPermissions(hasPermissions: Boolean) {
        _uiState.update { it.copy(hasPermissions = hasPermissions) }
    }
    fun updateDialogBT(dialogBT: Boolean) {
        _uiState.update { it.copy(dialogBT = dialogBT) }
    }
    fun updateShowDatePicker(showDatePicker: Boolean) {
        _uiState.update { it.copy(showDatePicker = showDatePicker) }
    }
    fun updateShowDeposit(showDeposit: Boolean) {
        _uiState.update { it.copy(showDeposit = showDeposit) }
    }
    fun updatePayTotalPayment(payTotalPayment: String) {
        _uiState.update { it.copy(payTotalPayment = payTotalPayment) }
    }
    fun updatePayObservation(payObservation: String) {
        _uiState.update { it.copy(payObservation = payObservation) }
    }
    fun updateDialogChoice(dialogChoice: Boolean) {
        _uiState.update { it.copy(dialogChoice = dialogChoice) }
    }
    fun updateSelect(select: SalesModel) {
        _uiState.update { it.copy(select = select) }
    }
    // endregion
    // region Date Picker
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState) {
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            val selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            _uiState.value = _uiState.value.copy(selectedDate = selectedDate)
            if (_uiState.value.dialogChoice) {
                _uiState.value = _uiState.value.copy(endDate = selectedDate.toString())
            } else {
                _uiState.value = _uiState.value.copy(startDate = selectedDate.toString())
            }
        }
    }
    // endregion
    // region
    fun getClients() {
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            val r = getClientsUseCase(internetUse)
            if (r.first != null) {
                _uiState.value = _uiState.value.copy(clients = r.first!!)
            }
            baseViewModel.hideLoader()
        }
    }

    fun filterPayments(): List<SalesModel> {
        return if (_uiState.value.search.text.isBlank()) {
            _uiState.value.sales
        } else {
            _uiState.value.sales.filter {
                it.nombreCliente!!.contains(_uiState.value.search.text, ignoreCase = true)
            }
        }
    }

    fun getPendingSales() {
        baseViewModel.showLoader()
        if (_uiState.value.startDate.isBlank() && _uiState.value.endDate.isBlank()) {
            _uiState.value = _uiState.value.copy(startDate = Helpers.getYesterday(), endDate = Helpers.getTomrrow())
        } else {
            if (_uiState.value.startDate.isBlank()) {
                _uiState.value = _uiState.value.copy(startDate = Helpers.getDate())
            }
            if (_uiState.value.endDate.isBlank()) {
                _uiState.value = _uiState.value.copy(endDate = Helpers.getTomrrow())
            }
        }
        viewModelScope.launch {
            val r = getPendingSalesUseCase("2", _uiState.value.startDate, _uiState.value.endDate, MainActivity.internetBtn.value)
            if (r.first != null) {
                Log.i("Sales___", r.first!!.toString())
                _uiState.value = _uiState.value.copy(sales = r.first!!)
            }
            baseViewModel.hideLoader()
        }
    }

    fun getPayment(ventaID: String) {
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            var r = getPaymentUseCase(ventaID, internetUse.value)
            if (r.first != null) {
                _uiState.value = _uiState.value.copy(dialogDeposit = true, payments = r.first!! as ArrayList<PayModel>)
            }
            baseViewModel.hideLoader()
        }
    }

    fun setPayment(ventaId: Int, montoPago: Double, observaciones: String, onSuccess: () -> Unit) {
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()) {
                val userSessionId = baseViewModel.getUsiarioId()
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                Log.i("UserID___", userSessionId.toString())
                val createPostSale = NewPayModel(
                    ventaId = ventaId,
                    montoPago = montoPago,
                    fecha = Helpers.getDateTime().replace(" ", "T"),
                    observaciones = observaciones,
                    origenId = 2,
                    tipoConexionId = if (internetUse) 1 else 2,
                    usuarioSesionId = userSessionId
                )
                var r = postPaymentUseCase(internetUse = internetUse, newPay = listOf(createPostSale))
                if (r.isSuccess) {
                    onSuccess()
                    MainActivity.mainDialogMsg.value = if (internetUse) "Pago realizado con exito" else "Pago guardado en modo offline"
                    MainActivity.mainDialog.value = true
                }
                getPendingSales()
            } else {
                baseViewModel.dialogLogin.value = true
            }
        }
    }

    fun deletePayment(pagoId: Int, deposit: PayModel, cnx: Context) {
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()) {
                val internetUse = Helpers.isInternetAvailable(cnx)
                var r = deletePaymentUseCase(internetUse, pagoId)
                if (r.isSuccess) {
                    _uiState.value = _uiState.value.copy(dialogDeposit = false)
                    MainActivity.mainDialogMsg.value = "Pago borrado exitosamente"
                    MainActivity.mainDialog.value = true
                    _uiState.value = _uiState.value.copy(payments = _uiState.value.payments.filter { it.ventaPagoId != pagoId } as ArrayList<PayModel>)
                } else {
                    MainActivity.mainDialogMsg.value = "Error al borrar el pago"
                    MainActivity.mainDialog.value = true
                }
                getPendingSales()
            } else {
                baseViewModel.dialogLogin.value = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun connectAndPrint(
        context: Context,
        device: BluetoothDevice
    ) {
        baseViewModel.showLoader()
        _uiState.value = _uiState.value.copy(dialogBT = false, dialogDeposit = false)
        withContext(Dispatchers.IO) {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            try {
                Log.i("Printer___",  _uiState.value.select?.fechaVenta!!.split("T")[0].toMexicanDate())
                val PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
                val vendedor = context.readPersistData(Constants.NOMBRE, "")
                var socket: BluetoothSocket? = null
                try {
                    socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID)
                    socket.connect()
                } catch (e: IOException) {
                    Log.e("Printer", "Fallo conexión normal, intentando fallback", e)
                    try {
                        val m: Method = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                        socket = m.invoke(device, 1) as BluetoothSocket
                        socket.connect()
                    } catch (e2: Exception) {
                        Log.e("Printer", "Fallo fallback", e2)
                        showToastOnMain(context, "No se pudo conectar con ${device.name}")
                        return@withContext
                    }
                }

                showToastOnMain(context, "Conectado a ${device.name}")

                val output = socket!!.outputStream
                repeat(2) {
                    printBitmap(context, output, R.drawable.casajordan)
                    val recivo = ("--------------------------------\n" +
                            "        Recibo de impresion\n" +
                            "Cliente: ${_uiState.value.select?.nombreCliente!!}\n" +
                            "Direccion: ${_uiState.value.select?.direccion!!}\n" +
                            "Folio: ${_uiState.value.select?.folio}  Total: $${_uiState.value.select?.total}\n" +
                            "--------------------------------\n" +
                            "Fecha de pago: ${_uiState.value.select?.fechaVenta!!.split("T")[0].toMexicanDate()}\n" +
                            "Monto pagado: ${_uiState.value.payTotalPayment}\n" +
                            "Saldo Restante: $${_uiState.value.select?.montoPorPagar!! -_uiState.value.payTotalPayment.toDouble()}\n" +
                            "Vendedor: $vendedor \n" +
                            "\n" +
                            "              FIRMA\n" +
                            "\n" +
                            "\n" +
                            " ____________________________\n" +
                            "\n" +
                            "\n" +
                            "\n").toByteArray()

                    output.write(recivo)
                    output.flush()
                    Thread.sleep(2200)
                }

                socket!!.close()
                baseViewModel.hideLoader()
                _uiState.value = _uiState.value.copy(dialogBT = false)
                showToastOnMain(context, "Impresión enviada correctamente")
                cleanDialog()
                MainActivity.mainDialogMsg.value = if (internetUse) "Pago realizado con exito" else "Pago guardado en modo offline"
                MainActivity.mainDialog.value = true
            } catch (e: Exception) {
                baseViewModel.hideLoader()
                _uiState.value = _uiState.value.copy(dialogBT = false)
                cleanDialog()
                MainActivity.mainDialogMsg.value = if (internetUse) "Pago realizado con exito" else "Pago guardado en modo offline"
                MainActivity.mainDialog.value = true
                showToastOnMain(context, "Error al imprimir: ${e.message}")
            }
        }
    }

    suspend fun connectAndReprint(
        context: Context,
        device: BluetoothDevice
    ) {
        baseViewModel.showLoader()
        _uiState.value = _uiState.value.copy(dialogBT = false, dialogDeposit = false)
        withContext(Dispatchers.IO) {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            try {
                Log.i("Printer1___",  "Saldo Restante: $${_uiState.value.select?.montoPorPagar}\n ${
                    _uiState.value.select?.fechaVenta!!.split(
                        "T"
                    )[0]
                }")
                val PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
                val vendedor = context.readPersistData(Constants.NOMBRE, "")
                var socket: BluetoothSocket? = null
                try {
                    socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID)
                    socket.connect()
                } catch (e: IOException) {
                    Log.e("Printer", "Fallo conexión normal, intentando fallback", e)
                    try {
                        val m: Method = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                        socket = m.invoke(device, 1) as BluetoothSocket
                        socket.connect()
                    } catch (e2: Exception) {
                        Log.e("Printer", "Fallo fallback", e2)
                        showToastOnMain(context, "No se pudo conectar con ${device.name}")
                        return@withContext
                    }
                }

                showToastOnMain(context, "Conectado a ${device.name}")

                val output = socket!!.outputStream
                printBitmap(context, output, R.drawable.casajordan)
                val recivo = ("--------------------------------\n" +
                        "        Recibo de impresion\n" +
                        "Cliente: ${_uiState.value.select?.nombreCliente!!}\n" +
                        "Direccion: ${_uiState.value.select?.direccion!!}\n" +
                        "Folio: ${_uiState.value.select?.folio}  Total: $${_uiState.value.select?.total}\n" +
                        "--------------------------------\n" +
                        "Fecha de pago: ${_uiState.value.select?.fechaVenta!!.split("T")[0].toMexicanDate()}\n" +
                        "Monto pagado: ${_uiState.value.select?.montoPagado}\n" +
                        "Saldo Restante: $${_uiState.value.select?.montoPorPagar!!}\n" +
                        "Vendedor: $vendedor \n" +
                        "\n" +
                        "              FIRMA\n" +
                        "\n" +
                        "\n" +
                        " ____________________________\n" +
                        "\n" +
                        "\n" +
                        "\n").toByteArray()

                output.write(recivo)
                output.flush()
                Thread.sleep(1100)

                socket.close()
                baseViewModel.hideLoader()
                _uiState.value = _uiState.value.copy(dialogBT = false)
                showToastOnMain(context, "Impresión enviada correctamente")
                cleanDialog()
                MainActivity.mainDialogMsg.value = if (internetUse) "Pago realizado con exito" else "Pago guardado en modo offline"
                MainActivity.mainDialog.value = true
            } catch (e: Exception) {
                baseViewModel.hideLoader()
                _uiState.value = _uiState.value.copy(dialogBT = false)
                cleanDialog()
                showToastOnMain(context, "Error al imprimir: ${e.message}")
                MainActivity.mainDialogMsg.value = if (internetUse) "Pago realizado con exito" else "Pago guardado en modo offline"
                MainActivity.mainDialog.value = true
            }
        }
    }
    suspend fun showToastOnMain(context: Context, message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    fun cleanDialog() {
        _uiState.value = _uiState.value.copy(
            dialogDeposit = false,
            dialogChoice = false,
            showDeposit = true,
            showDatePicker = false,
            selectedDate = LocalDate.now()
        )
        cleanPayment()
    }
}