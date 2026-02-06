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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.domain.sales.GetPendingSalesRepositoryImp
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.R
import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import com.example.inventariosapp.domain.repository.payment.DeletePaymentRepositoryImp
import com.example.inventariosapp.domain.repository.payment.GetPaymentRepositoryImp
import com.example.inventariosapp.domain.repository.payment.PostPaymentRepositoryImp
import com.example.inventariosapp.model.client.ClientResponseModel
import com.example.inventariosapp.model.payment.PayModel
import com.example.inventariosapp.model.sales.SalesModel
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
    private val getClientsUseCase: GetClientsRepositoryImp,
    private val getPendingSalesUseCase: GetPendingSalesRepositoryImp,
    private val getPaymentUseCase: GetPaymentRepositoryImp,
    private val postPaymentUseCase: PostPaymentRepositoryImp,
    private val deletePaymentUseCase: DeletePaymentRepositoryImp,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val baseViewModel = BaseViewModel()
    // region Date
    //val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    var selectedDate = mutableStateOf(LocalDate.now())
    var showDatePicker = mutableStateOf(false)

    val startDate = mutableStateOf("")
    val endDate = mutableStateOf("")
    val dialogChoice = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            selectedDate.value = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        if (dialogChoice.value) endDate.value = selectedDate.value.toString()
        else startDate.value = selectedDate.value.toString()
    }
    // endregion
    // region Clients list
    val clients: MutableState<List<ClientResponseModel>> = mutableStateOf(listOf())
    fun getClients(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getClientsUseCase(false)
            if (r.first != null){ clients.value = r.first!! }
            else {
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Sales
    val select: MutableState<SalesModel?> = mutableStateOf(null)
    val sales: MutableState<List<SalesModel>> = mutableStateOf(arrayListOf())
    fun getPendingSales(){
        baseViewModel.showLoader()
        if (startDate.value.isBlank() && endDate.value.isBlank()) {
            startDate.value = Helpers.getYesterday()
            endDate.value = Helpers.getTomrrow()
        } else {
            if (startDate.value.isBlank()) { startDate.value = Helpers.getDate() }
            if (endDate.value.isBlank()) { endDate.value = Helpers.getTomrrow() }
        }
        viewModelScope.launch {
            val r = getPendingSalesUseCase(startDate.value, endDate.value, false)
            if (r.first != null) {
                Log.i("Sales___", r.first!!.toString())
                sales.value = r.first!!
            }
            else{
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Dialog
    val payActualDate = Helpers.getDate()
    val payTotalPayment = mutableStateOf("")
    val payObservation = mutableStateOf("")
    val dialogDeposit = mutableStateOf(false)
    val showDeposit = mutableStateOf(false)
    val payments: MutableState<ArrayList<PayModel>> = mutableStateOf(arrayListOf())
    fun getPayment(ventaID: String){
        baseViewModel.showLoader()
        viewModelScope.launch {
            var r = getPaymentUseCase(ventaID, false)
            if (r.first != null){
                dialogDeposit.value = true
                payments.value = r.first!!
            }
            else{
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun setPayment(
        ventaId: Int,
        montoPago: Double,
        observaciones: String,
        cnx: Context,
    ){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val userID = cnx.readPersistData(Constants.USUARIO_ID, 0)
            Log.i("UserID___", userID.toString())
            val p = NewPayModel(
                ventaId = ventaId,
                montoPago = montoPago,
                fecha = Helpers.getDateTime().replace(" ", "T").plus("Z"),
                observaciones = observaciones,
                origenId = 1,
                tipoConexionId = 1,
                usuarioSesionId = userID
            )
            var r = postPaymentUseCase(newPay = listOf(p))
            if (r.isSuccess){
                MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                MainActivity.mainDialog.value = true
                cleanDialog()
            }
            else{
                MainActivity.mainDialogMsg.value = "Error 1001100"
                MainActivity.mainDialog.value = true
                cleanDialog()
            }
            getPendingSales()
        }
    }
    fun deletePayment(pagoId: Int, deposit: PayModel){
        baseViewModel.showLoader()
        viewModelScope.launch {
            var r = deletePaymentUseCase(pagoId)
            if (r.isSuccess){
                dialogDeposit.value = false
                MainActivity.mainDialogMsg.value = "Pago borrado exitosamente"
                MainActivity.mainDialog.value = true
                payments.value.remove(deposit)
            }
            else{
                MainActivity.mainDialogMsg.value = "Error al borrar el pago"
                MainActivity.mainDialog.value = true
            }
            getPendingSales()
        }
    }
    // endregion
    // region BT
    var dialogBT by mutableStateOf(false)
    val printerUUID = UUID.fromString(Constants.PRINTER_UUID)
    val bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter()
    val bondedDevices =  mutableStateListOf<BluetoothDevice>()
    var hasPermissions by  mutableStateOf(
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
    )

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
        dialogBT = false
        dialogDeposit.value = false
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
                        "        Recibo de impresión\n" +
                        "Cliente: ${select.value!!.nombreCliente!!}\n" +
                        "Direccion: ${select.value!!.direccion!!}\n" +
                        "Folio: ${select.value!!.folio}  Total: $${select.value!!.total}\n" +
                        "--------------------------------\n" +
                        "Fecha de pago: ${select.value!!.fechaVenta}\n" +
                        "Saldo Restante: $${select.value!!.montoPorPagar}\n" +
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
                dialogBT = false
                showToastOnMain(context, "Impresión enviada correctamente")
                cleanDialog()
            } catch (e: Exception) {
                baseViewModel.hideLoader()
                dialogBT = false
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
        payTotalPayment.value = ""
        payObservation.value = ""
    }
    fun cleanDialog(){
        dialogDeposit.value = false
        dialogChoice.value = false
        showDeposit.value = true
        showDatePicker.value = false
        selectedDate.value = LocalDate.now()
        cleanPayment()
    }
    // endregion
    init { getPendingSales() }
}