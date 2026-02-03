package com.example.inventariosapp.ui.view.sales

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventariosapp.R
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.ui.component.cards.CardSaleCmp
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Yellow
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesView(
    search: MutableState<TextFieldValue>,
    dateStart: String,
    dateEnd: String,
    data: MutableState<ArrayList<SalesModel>>,
    dialogChoice: MutableState<Boolean>,
    onSearchChangue: (TextFieldValue) -> Unit,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickDate: () -> Unit,
    onclickRow: (SalesModel) -> Unit,
    onClickAdd: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp).background(UI_Backround_Top),
                title = {
                    HeaderCmp(
                        title = "Ventas",
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
                modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White),
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
                        onChangeText ={ onSearchChangue(it) },
                        opcionContent = {},
                        labelText = "Busqueda",
                    )
                }
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                Row(
                    modifier = Modifier.height(75.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(PADDING_8).weight(1f),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){
                        InputWithTitleLabelCmp(
                            modifier = Modifier.weight(1f),
                            textFieldModifier = Modifier.clickable{
                                dialogChoice.value = false
                                onClickDate()
                            },
                            labelText = "Fecha Inicio",
                            textValue = dateStart,
                            onValueChange ={ it },
                            enabled = false,
                            disableBackgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .clickable { onClickDate() },
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(PADDING_8).weight(1f),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){
                        InputWithTitleLabelCmp(
                            modifier = Modifier.weight(1f),
                            textFieldModifier = Modifier.clickable{
                                dialogChoice.value = true
                                onClickDate()
                            },
                            labelText = "Fecha Fin",
                            textValue = dateEnd,
                            onValueChange ={ it },
                            enabled = false,
                            disableBackgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .clickable { onClickDate() },
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        )
                    }

                }
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
                        items(data.value) { client ->
                            var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                            CardSaleCmp(
                                modifier = Modifier.clickable { onclickRow(client) },
                                nameClient = client.nombreCliente.toString(),
                                folio = client.folio!!,
                                payLimitDate = client.fechaLimitePago.toString().take(10),
                                montoPagado = client.montoPagado.toString(),
                                montoPagar = client.montoPorPagar.toString(),
                                saleDate = client.fechaVenta.toString().take(10),
                                backgroundColor = colorRow
                            )
                            HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                            switchColor = !switchColor
                        }
                    }
                }

            }
        },
        floatingActionButton = {
            Card(
                modifier = Modifier.clickable{ onClickAdd() },
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardColors(
                    containerColor = UI_Backround_Btn_Yellow,
                    contentColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_add_white),
                    contentDescription = "Logo de la app",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun SalesViewPreview(){
    val opcions: MutableState<ArrayList<String>> =  mutableStateOf(arrayListOf())
    opcions.value.add("PArametro numero 1")
    opcions.value.add("PArametro numero 2")
    opcions.value.add("PArametro numero 3")
    opcions.value.add("PArametro numero 4")
    opcions.value.add("PArametro numero 5")
    SalesView(
        search = mutableStateOf(TextFieldValue("dfdfd")),
        dateEnd = "",
        dateStart = "",
        dialogChoice = remember { mutableStateOf(false) },
        onClickBack = {},
        onClickMenu = {},
        onClickDate = {},
        onclickRow = {},
        onClickAdd = {},
        data = mutableStateOf(arrayListOf()),
        onSearchChangue = {}
    )
}