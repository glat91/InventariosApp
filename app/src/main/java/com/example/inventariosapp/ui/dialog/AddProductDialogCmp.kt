package com.example.inventariosapp.ui.dialog

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.ui.component.ButtonWithImgCmp
import com.example.inventariosapp.ui.component.cards.CardProductCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.component.SelecPriceCmp
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Green
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Red
import com.example.inventariosapp.ui.view.new_sale.AddProductUiState

@Composable
fun rememberAvailableHeight(): Dp {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val imeInsets = WindowInsets.ime

    return remember(configuration, density, imeInsets) {
        derivedStateOf {
            val screenHeight = configuration.screenHeightDp.dp
            val imeHeight = with(density) {
                imeInsets.getBottom(this).toDp()
            }
            screenHeight - imeHeight - 300.dp
        }
    }.value
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialogCmp(
    uiState: AddProductUiState,
    search: TextFieldValue,
    opcions: ArrayList<ProductsResponseModel>,
    expanded: Boolean,
    product: ProductsResponseModel?,
    inventario: ProductIdResponseModel,
    onDismiss: () -> Unit,
    onChangeText: (TextFieldValue) -> Unit,
    onClickOpcion: (ProductsResponseModel) -> Unit,
    onClickPrice: (Double) -> Unit,
    onClickCancel: () -> Unit,
    onClickAccept: (ProductsResponseModel) -> Unit,
    // Note: In a real refactor, these updates would be handled via an Event/Intent pattern in the ViewModel
    onUpdateState: (AddProductUiState) -> Unit
) {
    // Logic for enabling button moved to side-effect of state changes
    LaunchedEffect(uiState.precio1, uiState.precio2, uiState.precio3, uiState.precio4, uiState.quantity) {
        val anyPriceSelected = uiState.precio1 || uiState.precio2 || uiState.precio3 || uiState.precio4
        val validQuantity = uiState.quantity.isNotEmpty() && uiState.quantity.toIntOrNull()?.let { it > 0 } == true
        onUpdateState(uiState.copy(enableBtn = anyPriceSelected && validQuantity))
    }

    Dialog(onDismissRequest = onDismiss) {
        Box {
            Column(
                modifier = Modifier.background(Color.White).padding(PADDING_8)
            ) {
                SearchBarCmp(
                    modifier = Modifier.padding(PADDING_8),
                    state = search, // Maintaining UI structure
                    labelText = "Ingrese el producto",
                    onClickClear = { onUpdateState(uiState.copy(expanded = false)) },
                    onChangeText = { onChangeText(it) },
                    opcionContent = {
                        DropdownMenu(
                            expanded = expanded && opcions.isNotEmpty(),
                            onDismissRequest = { onUpdateState(uiState.copy(expanded = false)) },
                            modifier = Modifier
                                .padding(top = PADDING_8)
                                .fillMaxWidth(.7f)
                                .heightIn(max = 240.dp)          // ← altura máxima fija
                                .windowInsetsPadding(WindowInsets.ime),
                            properties = PopupProperties(focusable = false)
                        ) {
                            Column(modifier = Modifier) {
                                opcions.forEach { option ->
                                    CardProductCmp(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onChangeText(TextFieldValue(option.descripcionPresentacion.toString()))
                                                onClickOpcion(option)
                                            },
                                        product = option.descripcionPresentacion.orEmpty(),
                                        backgroundColor = Color.White
                                    )
                                }
                            }
                        }
                    }
                )

                product?.let { currentProduct ->
                    if (currentProduct.precioVenta1 != null || currentProduct.precioVenta2 != null) {
                        Column(
                            modifier = Modifier
                                .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                                .padding(PADDING_8),
                        ) {
                            TextCmp(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Precios",
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight.Bold,
                                maxLine = 1,
                            )
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (currentProduct.precioVenta1 != null) {
                                        SelecPriceCmp(
                                            modifier = Modifier
                                                .padding(PADDING_8)
                                                .clickable {
                                                    onUpdateState(uiState.copy(precio1 = true, precio2 = false, precio3 = false, precio4 = false))
                                                    onClickPrice(currentProduct.precioVenta1)
                                                },
                                            precio = currentProduct.precioVenta1.toString(),
                                            selected = remember(uiState.precio1) { mutableStateOf(uiState.precio1) }
                                        )
                                    }
                                    if (currentProduct.precioVenta2 != null && currentProduct.precioVenta2 > 0.001){
                                        SelecPriceCmp(
                                            modifier = Modifier
                                                .padding(PADDING_8)
                                                .clickable {
                                                    onUpdateState(uiState.copy(precio1 = false, precio2 = true, precio3 = false, precio4 = false))
                                                    onClickPrice(currentProduct.precioVenta2)
                                                },
                                            precio = currentProduct.precioVenta2.toString(),
                                            selected = remember(uiState.precio2) { mutableStateOf(uiState.precio2) }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (currentProduct.precioVenta3 != null && currentProduct.precioVenta3 > 0.001) {
                                        SelecPriceCmp(
                                            modifier = Modifier
                                                .padding(PADDING_8)
                                                .clickable {
                                                    onUpdateState(uiState.copy(precio1 = false, precio2 = false, precio3 = true, precio4 = false))
                                                    onClickPrice(currentProduct.precioVenta3)
                                                },
                                            precio = currentProduct.precioVenta3.toString(),
                                            selected = remember(uiState.precio3) { mutableStateOf(uiState.precio3) }
                                        )
                                    }
                                    if (currentProduct.precioVenta4 != null && currentProduct.precioVenta4 > 0.001) {
                                        SelecPriceCmp(
                                            modifier = Modifier
                                                .padding(PADDING_8)
                                                .clickable {
                                                    onUpdateState(uiState.copy(precio1 = false, precio2 = false, precio3 = false, precio4 = true))
                                                    onClickPrice(currentProduct.precioVenta4)
                                                },
                                            precio = currentProduct.precioVenta4.toString(),
                                            selected = remember(uiState.precio4) { mutableStateOf(uiState.precio4) }
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Column(
                            modifier = Modifier
                                .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                                .padding(PADDING_8),
                        ) {
                            TextCmp(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Cantidad",
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight.Bold,
                                maxLine = 1,
                            )

                            InputWithTitleLabelCmp(
                                textValue = uiState.quantity,
                                modifier = Modifier,
                                labelText = "",
                                keyboardType = KeyboardType.Number,
                                onValueChange = { newValue ->
                                    val sanitized = if (newValue.startsWith("0") && newValue.length > 1) newValue.dropWhile { it == '0' } else newValue
                                    val value = sanitized.toIntOrNull() ?: 0
                                    if (value <= (inventario.inventario ?: 10000)) {
                                        onUpdateState(uiState.copy(quantity = sanitized))
                                    }
                                },
                                fontColor = Color.Black,
                            )
                            if(inventario.inventario != null){
                                TextCmp(
                                    modifier = Modifier.fillMaxWidth().padding(top = PADDING_8),
                                    text = "Total en inventario ${inventario.inventario}",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontStyle = FontStyle.Normal,
                                    textDecoration = TextDecoration.None,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.Bold,
                                    maxLine = 1,
                                )
                            }
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Column(
                            modifier = Modifier
                                .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                                .padding(PADDING_8),
                        ) {
                            TextCmp(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Comentarios",
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight.Bold,
                                maxLine = 1,
                            )

                            InputWithTitleLabelCmp(
                                textValue = uiState.comentarios,
                                modifier = Modifier,
                                labelText = "",
                                keyboardType = KeyboardType.Text,
                                textValueSize = 18.sp,
                                onValueChange = {
                                    onUpdateState(uiState.copy(comentarios = it))
                                },
                                fontColor = Color.Black,
                            )
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(PADDING_8),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ButtonWithImgCmp(
                                text = "Cancelar",
                                backGroundColor = UI_Backround_Btn_Red,
                                icon = Icons.Default.Close,
                                onClick = onClickCancel
                            )
                            VerticalDivider(thickness = PADDING_8, color = Color.Transparent)
                            ButtonWithImgCmp(
                                text = "Aceptar",
                                backGroundColor = UI_Backround_Btn_Green,
                                icon = Icons.Default.Add,
                                enable = uiState.enableBtn,
                                onClick = {
                                    if (uiState.enableBtn && uiState.quantity.isNotEmpty()) {
                                        onClickAccept(currentProduct)
                                        onDismiss()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductDialogCmpPreview(){
    val products: ArrayList<ProductsResponseModel> = arrayListOf()

    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    products.add(ProductsResponseModel(productoId=5437, descripcion ="TOALLITAS HUMEDAS  ABSORSEC 12/120", departamento="RENTA BODEGA", descripcionPresentacion="PAQUETE"))
    AddProductDialogCmp(
        uiState = AddProductUiState(),
        search = TextFieldValue(""),
        opcions = products,
        product = ProductsResponseModel(precioVenta1 = 10.0),
        onDismiss = {},
        onChangeText = {},
        onClickOpcion = { Log.i("Opcion___", it.toString()) },
        expanded = true,
        inventario = ProductIdResponseModel(inventario = 10),
        onClickPrice = {},
        onClickCancel = {},
        onClickAccept = {},
        onUpdateState = {},
    )
}