package com.example.inventariosapp.ui.view.BluetoothPrinterScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import java.io.OutputStream

fun printBitmap(context: Context, output: OutputStream, resId: Int, maxWidthPx: Int = 384) {
    // 1) Cargar y escalar manteniendo proporción
    val original = BitmapFactory.decodeResource(context.resources, resId)
    val scaled = if (original.width > maxWidthPx) {
        val newHeight = (original.height.toFloat() * (maxWidthPx.toFloat() / original.width.toFloat())).toInt()
        Bitmap.createScaledBitmap(original, maxWidthPx, newHeight, true)
    } else {
        original
    }

    // 2) Convertir a B/W (umbral simple)
    val bw = convertToBlackWhite(scaled)

    // 3) Preparar datos para GS v 0
    val width = bw.width
    val height = bw.height
    val widthBytes = (width + 7) / 8 // bytes por fila

    val bytes = ArrayList<Byte>()

    // GS v 0 : 0x1D 0x76 0x30 m  xL xH yL yH [data]
    // m = 0 (normal)
    bytes.add(0x1D.toByte()) // GS
    bytes.add(0x76.toByte()) // 'v'
    bytes.add(0x30.toByte()) // '0'
    bytes.add(0x00.toByte()) // m = 0

    val xL = (widthBytes and 0xFF).toByte()
    val xH = ((widthBytes shr 8) and 0xFF).toByte()
    val yL = (height and 0xFF).toByte()
    val yH = ((height shr 8) and 0xFF).toByte()

    bytes.add(xL)
    bytes.add(xH)
    bytes.add(yL)
    bytes.add(yH)

    // 4) Empaquetar cada fila (bits: MSB->LSB)
    for (y in 0 until height) {
        var x = 0
        for (bx in 0 until widthBytes) {
            var b = 0
            for (bit in 0..7) {
                val pixelX = x + bit
                val bitVal = if (pixelX < width) {
                    val color = bw.getPixel(pixelX, y)
                    if (color == Color.BLACK) 1 else 0
                } else {
                    0
                }
                b = (b shl 1) or bitVal
            }
            bytes.add((b and 0xFF).toByte())
            x += 8
        }
    }

    // 5) Escribir al output, usando .use para asegurar cierre si es necesario, 
    // pero aquí ya se pasa el stream abierto, así que solo escribimos y flush.
    output.write(bytes.toByteArray())
    output.flush()
    Thread.sleep(2200) // Sleep para que la impresora procese

    output.write(byteArrayOf(0x0A, 0x0A)) // 2 saltos de línea
    output.write(byteArrayOf(0x1B, 0x40)) // ESC @ -> Reset de impresora
    output.flush()
}

/** Convierte a B/W usando umbral promedio simple */
fun convertToBlackWhite(bitmap: Bitmap, threshold: Int = 127): Bitmap {
    val bw = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val p = bitmap.getPixel(x, y)
            val gray = (Color.red(p) * 0.3 + Color.green(p) * 0.59 + Color.blue(p) * 0.11).toInt()
            val color = if (gray < threshold) Color.BLACK else Color.WHITE
            bw.setPixel(x, y, color)
        }
    }
    return bw
}