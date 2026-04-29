package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.sales.SaleProductModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.ui.component.ButtonWithImgCmp
import com.example.inventariosapp.ui.component.cards.CardSellProductCmp
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_24
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Yellow
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2


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
            screenHeight - imeHeight - 200.dp
        }
    }.value
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSaleView(
    clientName: TextFieldValue,
    sale: SalesModel,
    salesData: SnapshotStateList<SaleProductModel>,
    opcions: ArrayList<ClientResponseModel>,
    expandedSearchBar: Boolean,
    canModify: Boolean,
    onChangueSearch: (TextFieldValue) -> Unit,
    onDissmissSearchBar: () -> Unit,
    onClickOpcion: (ClientResponseModel) -> Unit,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickProduct: () -> Unit,
    onClickSave: () -> Unit,
    onClickDelete: (SaleProductModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp).background(UI_Backround_Top),
                title = {
                    HeaderCmp(
                        title = if (sale.folio.isNullOrEmpty()) "Nueva venta" else "Editar Venta",
                        backActivate = true,
                        onClickBack = onClickBack,
                        onClickMenu = onClickMenu
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UI_Backround_Top,
                    titleContentColor = Color.White
                )
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(!sale.folio.isNullOrEmpty()){
                    TextCmp(
                        text = "Folio",
                        modifier = Modifier
                            .padding(PADDING_8)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        text = "${if(sale.folio == null)"" else sale.folio}",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(thickness = 15.dp, color = Color.Transparent)
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(start = PADDING_16, end = PADDING_16, top = PADDING_8),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    SearchBarCmp(
                        state = clientName,
                        labelText = "Nombre de cliente",
                        canModify = canModify,
                        onChangeText = {newText -> onChangueSearch(newText) },
                        opcionContent = {
                            val maxHeight = rememberAvailableHeight()
                            DropdownMenu(
                                expanded = expandedSearchBar && canModify,
                                onDismissRequest = { onDissmissSearchBar() },
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .padding()
                                    .heightIn(max = maxHeight),
                                properties = PopupProperties(focusable = false)
                            ) {
                                opcions.take(10).forEach { option ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(PADDING_4)
                                    ) {
                                        HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                                        TextCmp(
                                            option.nombreCliente.toString(),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onDissmissSearchBar()
                                                    onClickOpcion(option)
                                                },
                                            textAlign = TextAlign.Center,
                                            maxLine = 1
                                        )

                                    }
                                }
                                HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                            }
                        },
                    )
                }
                HorizontalDivider(thickness = 15.dp, color = Color.Transparent)
                Row(
                    modifier = Modifier
                        .padding(PADDING_8)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonWithImgCmp(
                        text = "Producto",
                        backGroundColor = UI_Backround_Btn_Yellow,
                        icon = Icons.Default.Add,
                        onClick = onClickProduct
                    )
                    ButtonWithImgCmp(
                        text = "Guardar",
                        backGroundColor = UI_Backround_Btn_Yellow,
                        icon = Icons.Filled.Create,
                        onClick = onClickSave
                    )
                }
                /*
                ButtonWithImgCmp(
                    text = "Salir",
                    backGroundColor = UI_Backround_Btn_Yellow,
                    icon = Icons.Default.Clear,
                    onClick = onClickBack
                )
                 */
                HorizontalDivider(thickness = 15.dp, color = Color.Transparent)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(PADDING_8),
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight().padding(bottom = it.calculateBottomPadding())
                    ) {
                        var switchColor = true
                        items(salesData) { prod ->
                            var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2

                            CardSellProductCmp(
                                producto = prod.nombreProducto,
                                quantity = prod.Cantidad.toString(),
                                sellPrice = prod.PrecioVenta.toString(),
                                backgroundColor = colorRow,
                                onClickDelete = { onClickDelete(prod) },
                            )

                            HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                            switchColor = !switchColor
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxWidth()
                    .padding(start = PADDING_8, end = PADDING_8, bottom = PADDING_24)
                ,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextCmp(
                    text = "SUBTOTAL:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                TextCmp(
                    text = if (sale.subtotal == null) "0.00" else sale.subtotal.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )

                TextCmp(
                    text = "IVA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                TextCmp(
                    text = if (sale.iva == null) "0.00" else sale.iva.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )


                TextCmp(
                    text = "TOTAL",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                TextCmp(
                    text = if (sale.total == null) "0.00" else sale.total.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NewSaleViewPreview(){
    val client =  TextFieldValue("")
    val opcions: MutableState<ArrayList<String>> =  remember {mutableStateOf(arrayListOf())}
    opcions.value.add("Parametro numero 1")
    opcions.value.add("Parametro numero 2")
    opcions.value.add("Parametro numero 3")
    opcions.value.add("Parametro numero 4")
    opcions.value.add("Parametro numero 5")
    NewSaleView(
        clientName = client,
        sale = SalesModel(folio = ""),
        salesData = remember { mutableStateListOf() },
        expandedSearchBar = true,
        canModify = false,
        opcions = arrayListOf(),
        onClickOpcion = {},
        onClickDelete = {},
        onClickBack = {},
        onClickProduct = {},
        onClickSave = { },
        onClickMenu = {},
        onChangueSearch = {},
        onDissmissSearchBar = {}
    )
}