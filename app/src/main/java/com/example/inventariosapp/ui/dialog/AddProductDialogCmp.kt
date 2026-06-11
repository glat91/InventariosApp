package com.example.inventariosapp.ui.dialog

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.ui.component.ButtonWithImgCmp
import com.example.inventariosapp.ui.component.cards.CardProductCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.component.SelecPriceCmp
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Green
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Red
import com.example.inventariosapp.ui.view.new_sale.AddProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialogCmp(
    uiState: AddProductUiState,
    search: TextFieldValue,
    opcions: ArrayList<ProductsResponseModel>,
    expanded: Boolean,
    onlineProduct: ProductsResponseModel?,
    offlineProduct:InventarioRseponeModel?,
    inventario: ProductIdResponseModel,
    onDismiss: () -> Unit,
    onChangeText: (TextFieldValue) -> Unit,
    onClickOpcion: (ProductsResponseModel) -> Unit,
    onClickPrice: (Double) -> Unit,
    onClickCancel: () -> Unit,
    onClickAccept: (ProductsResponseModel) -> Unit,
    onUpdateState: (AddProductUiState) -> Unit
) {
    LaunchedEffect(uiState.precio1, uiState.precio2, uiState.precio3, uiState.precio4, uiState.quantity) {
        val anyPriceSelected = uiState.precio1 || uiState.precio2 || uiState.precio3 || uiState.precio4
        val validQuantity = uiState.quantity.isNotEmpty() && uiState.quantity.toIntOrNull()?.let { it > 0 } == true
        onUpdateState(uiState.copy(enableBtn = anyPriceSelected && validQuantity))
    }

    Dialog(onDismissRequest = onDismiss) {
        Box {
            Column(
                modifier = Modifier.background(Color.White).padding(PADDING_8)
                    .verticalScroll(rememberScrollState())
            ) {
                SearchBarCmp(
                    modifier = Modifier.padding(PADDING_8),
                    state = search,
                    labelText = "Ingrese el producto",
                    onClickClear = { onUpdateState(uiState.copy(expanded = false)) },
                    onChangeText = { onChangeText(it) },
                    opcionContent = {
                        DropdownMenu(
                            expanded = expanded && opcions.isNotEmpty(),
                            onDismissRequest = { onUpdateState(uiState.copy(expanded = false)) },
                            modifier = Modifier
                                .fillMaxWidth(.7f)
                                .heightIn(max = 240.dp),
                            properties = PopupProperties(focusable = false)
                        ) {
                            Column(modifier = Modifier) {
                                opcions.take(10).forEach { option ->
                                    Log.i("Product___", option.toString())
                                    CardProductCmp(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onChangeText(TextFieldValue(option.descripcionPresentacion.toString()))
                                                onClickOpcion(option)
                                            },
                                        product = option.descripcionPresentacion.orEmpty(),
                                    )
                                }
                            }
                        }
                    }
                )

                onlineProduct?.let { currentProduct ->
                    if (currentProduct.precioVenta1 != null || currentProduct.precioVenta2 != null) {
                        data class PrecioItem(val valor: Double, val isSelected: Boolean, val index: Int)

                        val prices = listOf(
                            currentProduct.precioVenta1?.let { PrecioItem(it, uiState.precio1, 0) },
                            currentProduct.precioVenta2?.takeIf { it > 0.001 }?.let { PrecioItem(it, uiState.precio2, 1) },
                            currentProduct.precioVenta3?.takeIf { it > 0.001 }?.let { PrecioItem(it, uiState.precio3, 2) },
                            currentProduct.precioVenta4?.takeIf { it > 0.001 }?.let { PrecioItem(it, uiState.precio4, 3) },
                        ).filterNotNull()

                        val selectedPrice = prices.firstOrNull { it.isSelected }?.valor

                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(0.5.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TextCmp(
                                text = "PRECIOS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.1.em,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 12.dp, start = 2.dp)
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.heightIn(max = 300.dp)
                            ) {
                                itemsIndexed(prices) { _, item ->
                                    SelecPriceCmp(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                onUpdateState(uiState.copy(
                                                    precio1 = item.index == 0,
                                                    precio2 = item.index == 1,
                                                    precio3 = item.index == 2,
                                                    precio4 = item.index == 3,
                                                ))
                                                onClickPrice(item.valor)
                                            },
                                        label = "Precio ${item.index + 1}",
                                        precio = "$${item.valor}",
                                        selected = remember(item.isSelected) { mutableStateOf(item.isSelected) }
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextCmp(
                                    text = "Precio seleccionado",
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                                TextCmp(
                                    text = if (selectedPrice != null) "$$selectedPrice" else "—",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            }
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(0.5.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            CantidadSectionCmp(
                                uiState = uiState,
                                inventario = inventario,
                                offlineProduct = offlineProduct,
                                onUpdateState = { onUpdateState(it) }
                            )
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(0.5.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TextCmp(
                                text = "COMENTARIOS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.08.em,
                                color = Color.Black.copy(alpha = 0.35f)
                            )

                            BasicTextField(
                                value = uiState.comentarios,
                                onValueChange = { onUpdateState(uiState.copy(comentarios = it)) },
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color(0xFF1A1A1A),
                                    lineHeight = 24.sp
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                                cursorBrush = SolidColor(Color(0xFF1A1A1A)),
                                modifier = Modifier.fillMaxWidth(),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(minHeight = 96.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF7F7F7))
                                            .border(
                                                1.5.dp,
                                                Color.Black.copy(alpha = 0.08f),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .padding(horizontal = 14.dp, vertical = 12.dp)
                                    ) {
                                        if (uiState.comentarios.isEmpty()) {
                                            TextCmp(
                                                text = "Escribe un comentario...",
                                                fontSize = 15.sp,
                                                color = Color.Black.copy(alpha = 0.25f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            TextCmp(
                                text = "${uiState.comentarios.length} / 100",
                                fontSize = 11.sp,
                                color = Color.Black.copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        }

                        HorizontalDivider(thickness = PADDING_8, color = Color.Transparent)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(PADDING_8),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ButtonWithImgCmp(
                                text = "Cancelar",
                                backgroundColor = UI_Backround_Btn_Red,
                                icon = Icons.Default.Close,
                                onClick = onClickCancel
                            )
                            VerticalDivider(thickness = PADDING_8, color = Color.Transparent)
                            ButtonWithImgCmp(
                                text = "Aceptar",
                                backgroundColor = UI_Backround_Btn_Green,
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

@Composable
fun CantidadSectionCmp(
    uiState: AddProductUiState,
    inventario: ProductIdResponseModel?,
    offlineProduct: InventarioRseponeModel?,
    onUpdateState: (AddProductUiState) -> Unit
) {
    val stockLimit = inventario?.inventario ?: offlineProduct?.total
    val currentQty = uiState.quantity.toIntOrNull() ?: 0
    val atLimit = stockLimit != null && currentQty >= stockLimit
    val progress = if (stockLimit != null && stockLimit > 0)
        (currentQty.toFloat() / stockLimit).coerceIn(0f, 1f) else 0f

    val limitBorderColor by animateColorAsState(
        targetValue = if (atLimit) Color(0xFFE24B4A) else Color(0xFF3A3A3A),
        animationSpec = tween(200),
        label = "border"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextCmp(
            text = "CANTIDAD",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.08.em,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QtyButton(icon = Icons.Default.Remove) {
                val v = (currentQty - 1).coerceAtLeast(1)
                onUpdateState(uiState.copy(quantity = v.toString()))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .border(1.5.dp, limitBorderColor, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = uiState.quantity,
                    onValueChange = { newValue ->
                        val sanitized = if (newValue.startsWith("0") && newValue.length > 1)
                            newValue.dropWhile { it == '0' } else newValue
                        val value = sanitized.toIntOrNull() ?: 0
                        if (stockLimit == null || value <= stockLimit) {
                            onUpdateState(uiState.copy(quantity = sanitized))
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(Color.Black)
                )
            }

            QtyButton(icon = Icons.Default.Add) {
                val v = currentQty + 1
                if (stockLimit == null || v <= stockLimit) {
                    onUpdateState(uiState.copy(quantity = v.toString()))
                }
            }
        }

        AnimatedVisibility(
            visible = atLimit,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(180))
        ) {
            TextCmp(
                text = "Límite de inventario alcanzado",
                fontSize = 11.sp,
                color = Color(0xFFE24B4A),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (stockLimit != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextCmp(
                    text = "En inventario",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.38f)
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color.Black,
                    trackColor = Color.Black.copy(alpha = 0.1f)
                )
                TextCmp(
                    text = "$currentQty / $stockLimit",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun QtyButton(icon: ImageVector, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .border(1.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2E2E2E))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
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
        onlineProduct = ProductsResponseModel(precioVenta1 = 10.0),
        offlineProduct = null,
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
