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

@Composable
fun availableDropdownHeight(): Dp {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val imeHeight = with(density) { WindowInsets.ime.getBottom(this).toDp() }

    return screenHeight - imeHeight - 120.dp
}
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
    state: MutableState<TextFieldValue>,
    quantity: MutableState<String>,
    opcions: ArrayList<ProductsResponseModel>,
    inventario: ProductIdResponseModel,
    product: MutableState<ProductsResponseModel?>,
    comentarios: MutableState<String>,
    expanded: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onChangeText: (TextFieldValue) -> Unit,
    onClickOpcion: (ProductsResponseModel) -> Unit,
    onClickPrice: (Double) -> Unit,
    onClickCancel: () -> Unit,
    onClickAccept: (ProductsResponseModel) -> Unit,
) {
    // region Funcionalidad
    val precio1 = remember { mutableStateOf(false) }
    val precio2 = remember { mutableStateOf(false) }
    val precio3 = remember { mutableStateOf(false) }
    val precio4 = remember { mutableStateOf(false) }

    val enableBtn = remember{ mutableStateOf(false) }

    LaunchedEffect(precio1.value || precio2.value || precio3.value || precio4.value){
        if ((precio1.value || precio2.value || precio3.value || precio4.value)
            && !quantity.value.isNullOrEmpty()
        ){
            enableBtn.value = true
        }
    }
    LaunchedEffect(quantity.value){
        if ((precio1.value || precio2.value || precio3.value || precio4.value)
            && !quantity.value.isNullOrEmpty()
        ){
            if (quantity.value.toInt() > 0) enableBtn.value = true
            else enableBtn.value = false
        }
        else enableBtn.value = false
    }

    fun resetData(){
        precio1.value = false
        precio2.value = false
        precio3.value = false
        precio4.value = false
        quantity.value = ""
    }
    // endregion
    Dialog(onDismissRequest = onDismiss) {
        Box() {
            Column(
                modifier = Modifier.background(Color.White).padding(PADDING_8)
            ){
                SearchBarCmp(
                    modifier = Modifier.padding(PADDING_8),
                    state = state,
                    labelText = "Ingrese el producto",
                    onClickClear = { expanded.value = false },
                    onChangeText = { onChangeText(it) },
                    opcionContent = {
                        val maxHeight = rememberAvailableHeight()

                        DropdownMenu(
                            expanded = expanded.value && !opcions.isNullOrEmpty(),
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier
                                .padding(top = PADDING_8)
                                .fillMaxWidth(.7f)
                                //.heightIn(max = maxHeight)
                                .windowInsetsPadding(WindowInsets.ime),

                            properties = PopupProperties(focusable = false)
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                opcions.forEach { option ->
                                    CardProductCmp(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                expanded.value = false
                                                state.value = TextFieldValue(option.descripcionPresentacion.orEmpty())
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

                if (product.value?.precioVenta1 != null || product.value?.precioVenta2 != null){
                    Column(
                        modifier = Modifier
                            .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                            .padding(PADDING_8)
                        ,
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
                            ){
                                SelecPriceCmp(
                                    modifier = Modifier
                                        .padding(PADDING_8)
                                        .clickable {
                                            precio1.value = true
                                            precio2.value = false
                                            precio3.value = false
                                            precio4.value = false
                                            onClickPrice(product.value!!.precioVenta1!!)
                                        },
                                    precio = product.value!!.precioVenta1.toString(),
                                    selected = precio1
                                )
                                if (product.value?.precioVenta2 != null && product.value?.precioVenta2!! > 0.001){
                                    SelecPriceCmp(
                                        modifier = Modifier
                                            .padding(PADDING_8)
                                            .clickable {
                                                precio1.value = false
                                                precio2.value = true
                                                precio3.value = false
                                                precio4.value = false
                                                onClickPrice(product.value!!.precioVenta2!!)

                                            },
                                        precio = product.value!!.precioVenta2.toString(),
                                        selected = precio2
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                if (product.value?.precioVenta3 != null && product.value?.precioVenta3!! > 0.001){
                                    SelecPriceCmp(
                                        modifier = Modifier
                                            .padding(PADDING_8)
                                            .clickable {
                                                precio1.value = false
                                                precio2.value = false
                                                precio3.value = true
                                                precio4.value = false
                                                onClickPrice(product.value!!.precioVenta3!!)
                                            },
                                        precio = product.value!!.precioVenta3.toString(),
                                        selected = precio3
                                    )
                                }
                                if (product.value?.precioVenta4 != null && product.value?.precioVenta4!! > 0.001){
                                    SelecPriceCmp(
                                        modifier = Modifier
                                            .padding(PADDING_8)
                                            .clickable {
                                                precio1.value = false
                                                precio2.value = false
                                                precio3.value = false
                                                precio4.value = true
                                                onClickPrice(product.value!!.precioVenta4!!)
                                            },
                                        precio = product.value!!.precioVenta4.toString(),
                                        selected = precio4
                                    )
                                }
                            }
                        }
                        }

                    HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                    Column(
                        modifier = Modifier
                            .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                            .padding(PADDING_8)
                        ,
                    ){
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
                            textValue = quantity.value,
                            modifier = Modifier,
                            labelText = "Cantidad de productos",
                            keyboardType = KeyboardType.Number,
                            textValueSize = 18.sp,
                            onValueChange = {
                                // Permitir borrar
                                if (it.isEmpty()) {
                                    quantity.value = ""
                                    return@InputWithTitleLabelCmp
                                }

                                // Solo números
                                if (!it.all { it.isDigit() }) return@InputWithTitleLabelCmp

                                val sanitized = when {
                                    it == "0" -> "0"
                                    it.startsWith("0") -> it.dropWhile { it == '0' }
                                    else -> it
                                }

                                // Validar contra inventario
                                val value = sanitized.toIntOrNull() ?: return@InputWithTitleLabelCmp


                                if (value <= (inventario.inventario ?: 10000)) {
                                    quantity.value = sanitized
                                }
                                it
                            },
                            fontColor = Color.Black,
                        )
                        if (inventario.inventario != null){
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
                            .padding(PADDING_8)
                        ,
                    ){
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
                            textValue = comentarios.value,
                            modifier = Modifier,
                            labelText = "",
                            keyboardType = KeyboardType.Text,
                            textValueSize = 18.sp,
                            onValueChange = {
                                Log.i("Commets___", it.toString())
                                comentarios.value = it
                                it
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
                            enable = enableBtn.value,
                            onClick = {
                                if ((precio1.value || precio2.value || precio3.value || precio4.value) && quantity.value.isNotEmpty()){
                                    if (quantity.value.toInt() > 0){
                                        onClickAccept(product.value!!)
                                        onDismiss()
                                        resetData()
                                    }

                                }
                            }
                        )
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
        state = remember { mutableStateOf(TextFieldValue("")) },
        opcions = products,

        onDismiss = {},
        onChangeText = {},
        expanded = remember { mutableStateOf(false) },
        onClickOpcion = { Log.i("Opcion___", it.toString()) },
        product = remember {
            mutableStateOf(
                ProductsResponseModel(
                    precioVenta1 = 2.22,
                    precioVenta2 = 3.3,
                    precioVenta3 = 33.24,
                    precioVenta4 = 233.24

                )
            )
        },
        inventario = ProductIdResponseModel(inventario = 10),
        onClickPrice = {},
        comentarios = remember { mutableStateOf("") },
        quantity = remember { mutableStateOf("") },
        onClickCancel = {},
        onClickAccept = {}
    )
}