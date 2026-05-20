package com.example.inventariosapp.ui.view.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.ui.animations.AnimatedLazyColumn
import com.example.inventariosapp.ui.component.cards.CardSaleCmp
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.component.SearchBarCmp
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsView(
    search: TextFieldValue,
    dateStart: String,
    dateEnd: String,
    data: List<SalesModel>,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickDate: () -> Unit,
    onClickRow: (SalesModel) -> Unit,
    onChangueSearchBarTxt: (TextFieldValue) -> Unit,
    changueDialogChoice: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(0.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0F2027), // Izquierda
                                Color(0xFF203A43), // Centro
                                Color(0xFF2C5364)  // Derecha
                            )
                        )
                    ),
                title = {
                    HeaderCmp(
                        title = "Pagos",
                        onClickBack = onClickBack,
                        onClickMenu = onClickMenu
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    // Transparente para que se vea el gradient del modifier
                    containerColor = Color.Transparent,
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
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
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
                        onChangeText ={ txt -> onChangueSearchBarTxt(txt) },
                        labelText = "Nombre cliente"
                    )
                }
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
                                changueDialogChoice(false)
                                onClickDate()
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
                        modifier = Modifier.padding(PADDING_8),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){
                        InputWithTitleLabelCmp(
                            modifier = Modifier,
                            textFieldModifier = Modifier.clickable{
                                changueDialogChoice(true)
                                onClickDate()
                            },
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
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    AnimatedLazyColumn(
                        items = data,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        animationDurationMillis = 500,
                        initialOffsetX = (-48).dp,
                        initialBlurRadius = 12.dp,
                        staggerEnabled = true,
                        animateOnScroll = true,
                        key = { it },
                    ){ client ->

                        CardSaleCmp(
                            modifier = Modifier.clickable { onClickRow(client) },
                            nameClient = client.nombreCliente.toString(),
                            folio = client.folio.toString(),
                            payLimitDate = client.fechaLimitePago.toString().take(10),
                            montoPagado = client.montoPagado.toString(),
                            montoPagar = client.montoPorPagar.toString(),
                            saleDate = client.fechaVenta.toString().take(10),)
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
    PaymentsView(
        dateStart = "2025-06-07",
        dateEnd = "2025-06-07",
        search = TextFieldValue(""),
        onClickDate = {},
        onClickBack = {},
        onClickMenu = {},
        data = arrayListOf(),
        onClickRow = {},
        onChangueSearchBarTxt = {},
        changueDialogChoice = {  }
    )
}
