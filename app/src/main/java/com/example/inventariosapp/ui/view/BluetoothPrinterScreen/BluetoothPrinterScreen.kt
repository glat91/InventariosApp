package com.example.inventariosapp.ui.view.BluetoothPrinterScreen

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.inventariosapp.R
import com.example.inventariosapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Method
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothPrinterScreen(navController: NavHostController) {
    val printerUUID = UUID.fromString(Constants.PRINTER_UUID)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bluetoothAdapter = remember { BluetoothAdapter.getDefaultAdapter() }
    val bondedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    var hasPermissions by remember { mutableStateOf(false) }

    // --- PERMISOS ---
    val permissions = buildList {
        add(Manifest.permission.BLUETOOTH_CONNECT)
        add(Manifest.permission.BLUETOOTH_SCAN)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }.toTypedArray()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasPermissions = result.values.all { it }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions)
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Impresoras Bluetooth") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            if (!hasPermissions) {
                Text("Se necesitan permisos Bluetooth", modifier = Modifier)
                return@Column
            }

            val isEnabled = bluetoothAdapter?.isEnabled == true
            if (!isEnabled) {
                Text("Activa el Bluetooth e intenta de nuevo")
                return@Column
            }

            // Cargar dispositivos emparejados
            LaunchedEffect(Unit) {
                bondedDevices.clear()
                bluetoothAdapter.bondedDevices?.forEach { device ->

                    val hasPrinterUUID = device.uuids?.any { it.uuid == printerUUID } == true

                    val isImagingDevice = device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING

                    if (hasPrinterUUID || isImagingDevice) { bondedDevices.add(device) }
                }
            }

            if (bondedDevices.isEmpty()) {
                Text("No hay dispositivos emparejados")
            } else {
                LazyColumn {
                    items(bondedDevices) { device ->
                        Log.i("Items___", device.toString())
                        Text(
                            text = device.name ?: "No name",
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    connectAndPrint(
                                        clientName = "",
                                        clientDir = "",
                                        folio = "",
                                        total = "",
                                        date = "",
                                        balance = "",
                                        vendor = "",
                                        context = context,
                                        device = device,
                                    )
                                }
                            },
                            content = { Text(text = "Imprimir") },
                            )
                    }
                }
            }
        }
    }
}

// --- FUNCIÓN DE CONEXIÓN + IMPRESIÓN ---
@SuppressLint("MissingPermission")
suspend fun connectAndPrint(
    clientName: String,
    clientDir: String,
    folio: String,
    total: String,
    date: String,
    balance: String,
    vendor: String,
    context: Context,
    device: BluetoothDevice
) {
    withContext(Dispatchers.IO) {
        try {
            val PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
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
                    "Cliente: $clientName\n" +
                    "Direccion: $clientDir\n" +
                    "Folio: $folio  Total: $$total\n" +
                    "--------------------------------\n" +
                    "Fecha de pago: $date\n" +
                    "Saldo Restante: $$balance\n" +
                    "Vendedor: $vendor \n" +
                    "\n" +
                    "              FIRMA\n" +
                    "\n" +
                    "\n" +
                    " ____________________________\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n").toByteArray()

            output.write(recivo)
            output.flush()

            socket!!.close()
            showToastOnMain(context, "Impresión enviada correctamente")
        } catch (e: Exception) {
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
