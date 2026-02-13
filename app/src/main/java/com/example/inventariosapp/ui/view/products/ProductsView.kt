package com.example.inventariosapp.ui.view.products

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.ui.component.cards.CardInventoryCmp
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Cancel
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryView(
    search: MutableState<TextFieldValue>,
    data: MutableState<ArrayList<ProductsResponseModel>>,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp).background(UI_Backround_Top),
                title = {
                    HeaderCmp(
                        title = "Busqueda",
                        onClickBack = onClickBack,
                        onClickMenu = onClickMenu
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarColors(
                    containerColor = UI_List_Row_2,
                    scrolledContainerColor = UI_List_Row_2,
                    navigationIconContentColor = UI_List_Row_2,
                    titleContentColor = UI_List_Row_2,
                    actionIconContentColor =UI_List_Row_2,
                )
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                HorizontalDivider(thickness = 15.dp, color = Color.Transparent)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(start = PADDING_16, end = PADDING_16),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    SearchBarCmp(
                        state = search,
                        opcionContent = {},
                        onChangeText ={txt -> search.value = txt },
                        labelText = "Nombre producto"
                    )
                }
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(PADDING_8),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var switchColor = true
                        items(data.value) { product ->
                            var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                            product.costo
                            CardInventoryCmp(
                                producto = product.descripcionPresentacion!!,
                                departamento = product.departamento!!,
                                costo = product.costo.toString(),
                                precio1 = product.precioVenta1!!.toString(),
                                precio2 = if (product.precioVenta2 != null && product.precioVenta2 > 0) product.precioVenta2.toString() else null,
                                precio3 = if (product.precioVenta3 != null && product.precioVenta3 > 0) product.precioVenta3.toString() else null,
                                precio4 = if (product.precioVenta4 != null && product.precioVenta4 > 0) product.precioVenta4.toString() else null,
                                backgroundColor = if (product.esActivo!!) colorRow else UI_Backround_Btn_Cancel
                            )
                            HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                            switchColor = !switchColor
                        }
                    }
                    /*
                    InventoryListCmp(
                        titleColumn = arrayListOf("Codigo", "Descripcion", "Precio 1", "Precio 2"),
                        dbData = data,
                        onClickRow = {
                            Log.i("DataRow___", it.toString())
                        })

                     */
                }

            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SearchViewPreview(){
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000
    )
    InventoryView(
        search = remember { mutableStateOf(TextFieldValue("")) },
        onClickBack = {},
        onClickMenu = {},
        data = mutableStateOf( arrayListOf())
    )
}