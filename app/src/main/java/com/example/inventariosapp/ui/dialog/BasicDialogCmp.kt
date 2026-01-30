package com.example.inventariosapp.ui.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.ui.component.ButtonCmp
import com.example.inventariosapp.ui.component.ButtonWithImgCmp
import com.example.inventariosapp.ui.component.cards.CardDepositCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Accept
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Cancel
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Green
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Yellow
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2

@Composable
fun BasicDialogCmp(
    color: Color,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card del contenido principal
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp), // espacio para que entre el icono
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                content()
            }
            // Icono flotante arriba del Card
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color, CircleShape)
                    .border(2.dp, Color.White, CircleShape), // borde blanco para que resalte
                contentAlignment = Alignment.Center
            ) {
                TextCmp(
                    text = "!",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicDialogCmpPreviewq(){
    // region var
    val model = arrayListOf<SalesModel>()
    model.add(
        SalesModel(
            ventaId = 1,
            nombreCliente = "Juan Pérez",
            folio = "FOL-1001",
            subtotal = 1500.50,
            descuento = 100,
            iva = 240.08,
            total = 1640.58,
            montoPagado = 1000,
            montoPorPagar = 640.58,
            fechaVenta = "2025-09-23",
            fechaVentaFormato = "23/09/2025",
            estatusVentaId = 1,
            estatusVenta = "Pagada",
            direccion = "Av. Siempre Viva 742",
            diasCredito = 30,
            fechaLimitePago = "2025-10-23"
        )
    )
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500,
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
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500,
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
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500,
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
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500,
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
    model.add(
        SalesModel(
            ventaId = 2,
            nombreCliente = "María López",
            folio = "FOL-1002",
            subtotal = 2000.00,
            descuento = 200,
            iva = 288.00,
            total = 2088.00,
            montoPagado = 500,
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
    // endregion
    val total = remember {mutableStateOf("100,999,999.00")}
    val adeudo = remember {mutableStateOf("10,999,999.00")}
    BasicDialogCmp(
        color = UI_Backround_Top,
        content = {
            val showDeposit = remember { mutableStateOf(false) }
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
                                text = "${total.value}",
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
                                text = "${adeudo.value}",
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
                    ){

                    }
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
                                onClick = {
                                    showDeposit.value = false
                                }
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
                                items(model) { deposit ->
                                    var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                                    CardDepositCmp(
                                        date = deposit.fechaVenta.toString(),
                                        totalAmount = deposit.total.toString(),
                                        observations = deposit.nombreCliente.toString(),
                                        backgroundColor = colorRow,
                                        onClickDelete = {},
                                        onClickPrint = {}
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
                                textValue = "",
                                onValueChange = { it },
                                textAlign = TextAlign.Left,
                                disableTextColor = Color.Gray,
                                enabled = true,
                            )
                            InputWithTitleLabelCmp(
                                modifier = Modifier,
                                labelText = "Importe",
                                keyboardType = KeyboardType.Number,
                                textValue = "",
                                onValueChange = { it },
                                textAlign = TextAlign.Left,
                                disableTextColor = Color.Gray,
                                enabled = true,
                            )
                            InputWithTitleLabelCmp(
                                modifier = Modifier,
                                labelText = "Observaciones",
                                keyboardType = KeyboardType.Number,
                                textValue = "",
                                onValueChange = { it },
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
                                    onClick = {},
                                    enable = true,
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
        onDismiss = {}
    )
}