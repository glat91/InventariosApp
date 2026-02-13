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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.TEXT_List_Title
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import com.example.inventariosapp.ui.theme.UI_List_Title

@Composable
fun DepositListCmp(
    titleColumn: List<String>,
    dbData: List<SalesModel>,
    onClickRow: (SalesModel) -> Unit
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
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextCmp(
                text = titleColumn.get(0),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
            TextCmp(
                text = titleColumn.get(1),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
            TextCmp(
                text = titleColumn.get(2),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
        }
        // Lista con los datos
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            var switchColor = true
            items(dbData) {
                var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                        .background(colorRow)
                        .padding(8.dp)
                        .clickable { onClickRow(it) }
                    ,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    TextCmp(
                        maxLine = 1,
                        text = it.folio.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )

                    TextCmp(
                        maxLine = 4,
                        text = it.fechaVenta.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        maxLine = 4,
                        text = it.nombreCliente.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                    Card(
                        modifier = Modifier.padding(PADDING_4),
                        //shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.elevatedCardElevation(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                    Card(
                        modifier = Modifier.padding(PADDING_4),
                        //shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.elevatedCardElevation(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
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
fun DepositListCmpPreview(){
    val model = arrayListOf<SalesModel>()
    model.add(SalesModel(
        ventaId = 1,
        nombreCliente = "Juan Pérez",
        folio = "FOL-1001",
        subtotal = 1500.50,
        descuento = 100,
        iva = 240.08,
        total = 1640.58,
        montoPagado = 1000.00,
        montoPorPagar = 640.58,
        fechaVenta = "2025-09-23",
        fechaVentaFormato = "23/09/2025",
        estatusVentaId = 1,
        estatusVenta = "Pagada",
        direccion = "Av. Siempre Viva 742",
        diasCredito = 30,
        fechaLimitePago = "2025-10-23"
    ))
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500.00,
            montoPorPagar = 1588.00,
            fechaVenta = "2025-09-22",
            fechaVentaFormato = "22/09/2025",
            estatusVentaId = 2,
            estatusVenta = "Pendiente",
            direccion = "Calle Falsa 123",
            diasCredito = 15,
            fechaLimitePago = "2025-10-07"
        )
    )
    DepositListCmp(
        titleColumn = arrayListOf("Folio", "Fecha", "Cliente"),
        dbData = model,
        onClickRow = {  }
    )
}