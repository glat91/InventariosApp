package com.example.inventariosapp.ui.view.BluetoothPrinterScreen

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.inventariosapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Method
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothPrinterScreen(navController: NavHostController) {
    val printerUUID = UUID.fromString(Constants.PRINTER_UUID)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bluetoothAdapter = remember { BluetoothAdapter.getDefaultAdapter() }
    val bondedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    
    fun hasBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    var hasPermissions by remember { mutableStateOf(hasBluetoothPermissions()) }

    // --- PERMISOS ---
    val permissions = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_CONNECT)
            add(Manifest.permission.BLUETOOTH_SCAN)
        }
    }.toTypedArray()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasPermissions = result.values.all { it }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions) {
            permissionLauncher.launch(permissions)
        }
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Impresoras Bluetooth") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            if (!hasPermissions) {
                Text("Se necesitan permisos Bluetooth")
                Button(onClick = { permissionLauncher.launch(permissions) }) {
                    Text("Conceder permisos")
                }
                return@Column
            }

            val isEnabled = bluetoothAdapter?.isEnabled == true
            if (!isEnabled) {
                Text("Activa el Bluetooth e intenta de nuevo")
                return@Column
            }

            // Cargar dispositivos emparejados
            LaunchedEffect(hasPermissions) {
                if (hasPermissions) {
                    bondedDevices.clear()
                    try {
                        @SuppressLint("MissingPermission")
                        val bonded = bluetoothAdapter.bondedDevices
                        bonded?.forEach { device ->
                            val hasPrinterUUID = device.uuids?.any { it.uuid == printerUUID } == true
                            val isImagingDevice = device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING
                            if (hasPrinterUUID || isImagingDevice) {
                                bondedDevices.add(device)
                            }
                        }
                    } catch (e: SecurityException) {
                        Log.e("BluetoothPrinter", "Permission missing when accessing bondedDevices", e)
                    }
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
                                if (hasBluetoothPermissions()) {
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
                                } else {
                                    permissionLauncher.launch(permissions)
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
        var socket: BluetoothSocket? = null
        try {
            val PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            
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

            // Usamos un bloque para asegurar el cierre del stream
            socket.outputStream.use { output ->
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
                
                // Pequeña pausa para asegurar que el buffer se envíe antes de cerrar
                Thread.sleep(1000)
            }

            socket.close()
            showToastOnMain(context, "Impresión enviada correctamente")
        } catch (e: SecurityException) {
            Log.e("Printer", "SecurityException: falta permiso BLUETOOTH_CONNECT", e)
            showToastOnMain(context, "Error de permisos al conectar")
        } catch (e: IOException) {
            Log.e("Printer", "IOException: $e", e)
            showToastOnMain(context, "Error de conexión: ${e.message}")
        } catch (e: Exception) {
            Log.e("Printer", "Exception: $e", e)
            showToastOnMain(context, "Error al imprimir: ${e.message}")
        } finally {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e("Printer", "Error al cerrar socket", e)
            }
        }
    }
}

// --- Toast seguro desde hilo ---
suspend fun showToastOnMain(context: Context, message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
