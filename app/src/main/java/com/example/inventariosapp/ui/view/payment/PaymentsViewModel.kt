package com.example.inventariosapp.ui.view.payment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Method
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

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
    var uiState by mutableStateOf(PaymentsUiState())
    // region Date

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            setSelectDate(
                Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        }
        if (uiState.dialogChoice) {
            setEndDate(uiState.selectedDate.toString())
        }
        else {
            setStartDate(uiState.selectedDate.toString())
        }
    }
    // endregion
    // region Clients list
    fun getClients(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val r = getClientsUseCase(internetUse.value)
            if (r.first != null){ uiState.clients = r.first!! }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Sales
    fun getPendingSales(){
        baseViewModel.showLoader()
        if (uiState.startDate.isBlank() && uiState.endDate.isBlank()) {
            setStartDate(Helpers.getDate())
            setEndDate(Helpers.getTomrrow())
        }
        else {
            if (uiState.startDate.isBlank()) { setStartDate(Helpers.getDate()) }
            if (uiState.endDate.isBlank()) { setEndDate(Helpers.getTomrrow()) }
        }
        viewModelScope.launch {
            val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val r = getPendingSalesUseCase(uiState.startDate, uiState.endDate, internetUse.value)
            if (r.first != null) { setSales(r.first!!) }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Dialog
    fun getPayment(ventaID: String){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            var r = getPaymentUseCase(ventaID, internetUse.value)
            if (r.first != null){
                setDialogDeposit(true)
                setPayments(r.first!! as ArrayList<PayModel>)
            }
            baseViewModel.hideLoader()
        }
    }
    fun setPayment(ventaId: Int, montoPago: Double, observaciones: String, onSuccess: () -> Unit){
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()){
                val userID = baseViewModel.getUsiarioId()
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                Log.i("UserID___", userID.toString())
                val createPostSale = NewPayModel(
                    ventaId = ventaId,
                    montoPago = montoPago,
                    fecha = Helpers.getDateTime().replace(" ", "T"),
                    observaciones = observaciones,
                    origenId = userID,
                    tipoConexionId = if (internetUse) 1 else 2,
                    usuarioSesionId = userID
                )
                onSuccess()
                var r = postPaymentUseCase(internetUse = internetUse, newPay = listOf(createPostSale))
                if (r.isSuccess){
                    MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                    MainActivity.mainDialog.value = true

                }
                cleanDialog()
                getPendingSales()
            }
            else{
                baseViewModel.dialogLogin.value = true
            }
        }
    }
    fun deletePayment(pagoId: Int, deposit: PayModel, cnx: Context){
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()){
                val internetUse = Helpers.isInternetAvailable(cnx) || MainActivity.internetBtn.value
                var r = deletePaymentUseCase(internetUse, pagoId)
                if (r.isSuccess){
                    setDialogDeposit(false)
                    MainActivity.mainDialogMsg.value = "Pago borrado exitosamente"
                    MainActivity.mainDialog.value = true
                    uiState.payments.remove(deposit)
                }
                else{
                    MainActivity.mainDialogMsg.value = "Error al borrar el pago"
                    MainActivity.mainDialog.value = true
                }
                getPendingSales()
            }
            else{
                baseViewModel.dialogLogin.value = true
            }

        }
    }
    // endregion
    // region BT
    val permissions = buildList {
        add(Manifest.permission.BLUETOOTH_CONNECT)
        add(Manifest.permission.BLUETOOTH_SCAN)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }.toTypedArray()
    @SuppressLint("MissingPermission")
    suspend fun connectAndPrint(
        context: Context,
        device: BluetoothDevice
    ) {
        baseViewModel.showLoader()
        setDialogBT(false)
        setDialogDeposit(false)
        withContext(Dispatchers.IO) {
            try {
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
                        "Cliente: ${uiState.select!!.nombreCliente!!}\n" +
                        "Direccion: ${uiState.select!!.direccion!!}\n" +
                        "Folio: ${uiState.select!!.folio}  Total: $${uiState.select!!.total}\n" +
                        "--------------------------------\n" +
                        "Fecha de pago: ${uiState.select!!.fechaVenta}\n" +
                        "Saldo Restante: $${uiState.select!!.montoPorPagar}\n" +
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

                socket!!.close()
                baseViewModel.hideLoader()
                setDialogBT(false)
                showToastOnMain(context, "Impresión enviada correctamente")
                cleanDialog()
            } catch (e: Exception) {
                baseViewModel.hideLoader()
                setDialogBT(false)
                cleanDialog()
                showToastOnMain(context, "Error al imprimir: ${e.message}")
            }
        }
    }

    // --- Toast seguro desde hilo ---
    suspend fun showToastOnMain(context: Context, message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    //endregion
    // region clean
    fun cleanPayment(){
        setPayTotalPayment("")
        setPayObservation("")
    }
    fun cleanDialog(){
        setDialogDeposit(false)
        setDialogChoice(false)
        setShowDeposit(true)
        setShowDatePicker(false)
        setSelectDate(LocalDate.now())
        cleanPayment()
    }
    // endregion
    // region changue uiState
    fun setSelectDate(data: LocalDate){ uiState = uiState.copy(selectedDate = data) }
    fun setShowDatePicker(data: Boolean){ uiState = uiState.copy(showDatePicker = data) }

    fun setStartDate(data: String){ uiState = uiState.copy(startDate = data) }
    fun setEndDate(data: String){ uiState = uiState.copy(endDate = data) }
    fun setDialogChoice(data: Boolean){ uiState = uiState.copy(dialogChoice = data) }

    fun setClients(data: List<ClientResponseModel>){ uiState = uiState.copy(clients = data) }

    fun setSelect(data: SalesModel?){ uiState = uiState.copy(select = data) }
    fun setSales(data: List<SalesModel>){ uiState = uiState.copy(sales = data) }

    fun setPayActualDate(data: String){ uiState = uiState.copy(payActualDate = data) }
    fun setPayTotalPayment(data: String){ uiState = uiState.copy(payTotalPayment = data) }
    fun setPayObservation(data: String){ uiState = uiState.copy(payObservation = data) }
    fun setDialogDeposit(data: Boolean){ uiState = uiState.copy(dialogDeposit = data) }
    fun setShowDeposit(data: Boolean){ uiState = uiState.copy(showDeposit = data) }
    fun setPayments(data: ArrayList<PayModel>){ uiState = uiState.copy(payments = data) }

    fun setDialogBT(data: Boolean){ uiState = uiState.copy(dialogBT = data) }
    fun setPrinterUUID(data: UUID){ uiState = uiState.copy(printerUUID = data) }
    fun setBluetoothAdapter(data: BluetoothAdapter){ uiState = uiState.copy(bluetoothAdapter = data) }
    fun setBondedDevices(data: SnapshotStateList<BluetoothDevice>){ uiState = uiState.copy(bondedDevices = data) }
    fun setHasPermissions(data: Boolean){ uiState = uiState.copy(hasPermissions = data) }

    fun setPermissions(data: List<String>){ uiState = uiState.copy(permissions = data) }
    // endregion
    init {
        getPendingSales()
    }
}
data class PaymentsUiState(
    var selectedDate: LocalDate = LocalDate.now(),
    var showDatePicker: Boolean = false,

    var startDate: String = "",
    var endDate: String = "",
    var dialogChoice: Boolean = false,

    var clients: List<ClientResponseModel> = listOf(),

    var select: SalesModel? = null,
    var sales: List<SalesModel> = arrayListOf(),

    var payActualDate: String = Helpers.getDate(),
    var payTotalPayment: String = "",
    var payObservation: String = "",
    var dialogDeposit: Boolean = false,
    var showDeposit: Boolean = false,
    var payments: ArrayList<PayModel> = arrayListOf(),

    var dialogBT: Boolean = false,
    val printerUUID: UUID = UUID.fromString(Constants.PRINTER_UUID),
    val bluetoothAdapter: BluetoothAdapter =  BluetoothAdapter.getDefaultAdapter(),
    var bondedDevices: SnapshotStateList<BluetoothDevice> =  mutableStateListOf<BluetoothDevice>(),
    var hasPermissions: Boolean = false,

    var permissions: List<String> = emptyList()
)