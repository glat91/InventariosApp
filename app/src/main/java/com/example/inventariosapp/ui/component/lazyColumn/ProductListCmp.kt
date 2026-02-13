package com.example.inventariosapp.ui.component.lazyColumn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.domain.model.sales.SaleProductModel
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.TEXT_List_Title
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import com.example.inventariosapp.ui.theme.UI_List_Title

@Composable
fun ProductListCmp(
    titleColumn: List<String>,
    dbData: MutableState<ArrayList<SaleProductModel>>,
    image: ImageVector? = null,
    onClickRow: (SaleProductModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(UI_List_Title)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextCmp(
                text = titleColumn.get(0),
                color = TEXT_List_Title,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            TextCmp(
                text = titleColumn.get(1),
                color = TEXT_List_Title,
                modifier = Modifier.width(120.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            TextCmp(
                text = titleColumn.get(2),
                color = TEXT_List_Title,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            TextCmp(
                text = titleColumn.get(3),
                color = TEXT_List_Title,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            image.let {
                TextCmp(
                    text = titleColumn.get(4),
                    modifier = Modifier.width(60.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        // Lista con los datos
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            var switchColor = true
            items(dbData.value) {
                var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                        .background(colorRow)
                        .padding(8.dp)
                        .clickable {
                            dbData.value = ArrayList(dbData.value).apply { remove(it) }
                            onClickRow(it) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextCmp(
                        maxLine = 1,
                        text = it.Cantidad.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )

                    TextCmp(
                        maxLine = 4,
                        text = it.ProductoId.toString(),
                        modifier = Modifier.width(120.dp),
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        maxLine = 4,
                        text = it.PrecioVenta.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        maxLine = 4,
                        text = ((it.Cantidad?.toDouble() ?: 0.0) * (it.PrecioVenta ?: 0.0)).toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                    image?.let {
                        Card(
                            modifier = Modifier.padding(PADDING_4),
                            //shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = "",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                switchColor = !switchColor
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListCmpPreview(){
    // region Var
    val sale = mutableStateOf(
        GetSalesByIdResponse(
            ventaId = 51669,
            clienteId = 47,
            estatusVentaId = 2,
            nombreCliente = null,
            folio = "",
            subtotal = 1065.79,
            descuento = 0.00,
            iva = 203.010,
            retencion = 0.000,
            total = 1268.80,
            fechaVenta = "2025-09-10T23:52:27.67",
            fechaVentaFormato = "10/09/2025",
            sucursalId = 6,
            almacenId = 4,
            usuario = "admin",
            montoPagado = 50.00,
            montoPorPagar = 1218.80,
            tipoPagoId = 1,
            esFueraDeLinea = false,
            origenId = 1,
            tipoConexionId = 1,
            ventaIdInterno = null,
            version = null,
            ventaProductos = arrayListOf(
                SaleProductModel(
                    VentaProductoId = 283885,
                    VentaId = 51669,
                    ProductoId = 8817,
                    Cantidad = 1,
                    PrecioVenta = 718.800000,
                    Costo = 539.100000,
                    CantidadSolicitada = 0,
                    VentaIdInterno = null,
                    Venta = null
                ),
                SaleProductModel(
                    VentaProductoId = 283886,
                    VentaId = 51669,
                    ProductoId = 13,
                    Cantidad = 1,
                    PrecioVenta = 550.000000,
                    Costo = 495.000000,
                    CantidadSolicitada = 0,
                    VentaIdInterno = null,
                    Venta = null
                )
            ),
            cliente = ClientResponseModel(
                clienteId = 47,
                tipoPersonaId = 2,
                tipoPersona = null,
                tipoGiroId = 6,
                tipoGiro = null,
                tipoPagoId = 2,
                tipoPago = null,
                nombreCliente = "ARACELI BIBIANO HERNANDEZ",
                razonSocial = "",
                rfc = "",
                curp = "",
                paisId = 1,
                estadoId = 30,
                ciudadId = 156,
                localidad = "IXCANELCO",
                direccion = "C. LEONA VICARIO",
                colonia = "",
                calle = "C. LEONA VICARIO",
                codigoPostal = "92100",
                noExterior = "S/N",
                noInterior = "",
                telefono = "7891125480",
                correo = "",
                montoCredito = 13880.00,
                diasCredito = 22,
                usuarioSesionId = 1027,
                fechaIngreso = "2020-12-04T22:50:08.217",
                fechaModifico = "2023-03-01T14:14:48.933",
                esActivo = true,
                estatus = null
            ),
            direccion = "C. LEONA VICARIO S/N ,TANTOYUCA Veracruz",
            usuarioSesionId = 1,
            fechaIngreso = "2025-09-10T23:52:27.69",
            fechaModifico = "2025-09-22T12:08:18.113",
            esActivo = true,
            estatus = null
        )
    )
    val products = remember {mutableStateOf(sale.value.ventaProductos)}
    // endregion
    ProductListCmp(
        titleColumn = arrayListOf("Cant", "Producto", "Precio Unitario", "Total", ""),
        dbData = remember { mutableStateOf(arrayListOf()) },
        image = Icons.Default.Delete,
        onClickRow = {},
    )

}