package com.example.inventariosapp.ui.view.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.ui.component.cards.CardSaleCmp
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import java.time.LocalDate


// TODO mostrar inmediato el tipo de pago de credito o contado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsView(
    dateStart: String,
    dateEnd: String,
    data: List<SalesModel>,
    clickDate:Boolean,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickDate: (Boolean) -> Unit,
    onClickRow: (SalesModel) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp).background(UI_Backround_Top),
                title = {
                    HeaderCmp(
                        title = "Pagos",
                        backActivate = true,
                        onClickBack = onClickBack,
                        onClickMenu = onClickMenu
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors (
                    containerColor = UI_Backround_Top,     // Morado
                    titleContentColor = Color.White
                )
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO falta serchBar
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                Row {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(PADDING_8),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){

                        InputWithTitleLabelCmp(
                            modifier = Modifier.fillMaxWidth(.5f),
                            textFieldModifier = Modifier.clickable{
                                //clickDate = false
                                onClickDate(false)
                            },
                            labelText = "Fecha",
                            textValue = dateStart,
                            onValueChange ={ it },
                            enabled = false,
                            disableBackgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .clickable { onClickDate(false) },
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
                        modifier = Modifier.padding(PADDING_8),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){
                        InputWithTitleLabelCmp(
                            modifier = Modifier,
                            textFieldModifier = Modifier.clickable{
                                //clickDate = true
                                onClickDate(true) },
                            labelText = "Fecha",
                            textValue = dateEnd,
                            onValueChange ={ it },
                            enabled = false,
                            disableBackgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .clickable { onClickDate(false) },
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
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var switchColor = true
                        items(data) { client ->
                            var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                            CardSaleCmp(
                                modifier = Modifier.clickable { onClickRow(client) },
                                nameClient = client.nombreCliente.toString(),
                                folio = client.folio.toString(),
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
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SellViewPrevie(){
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    PaymentsView(
        dateStart = "2025-06-07",
        dateEnd = "2025-06-07",
        clickDate =false,
        onClickDate = {},
        onClickBack = {},
        onClickMenu = {},
        data = arrayListOf(),
        onClickRow = {},
    )
}
