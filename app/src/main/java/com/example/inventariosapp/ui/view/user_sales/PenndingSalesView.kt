package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventariosapp.local.entity.PostSaleWithProducts
import com.example.inventariosapp.ui.component.GlassButton
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.cards.CardPenndingSaleCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.util.CustomEnums

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenndingSalesView(
    data: ArrayList<PostSaleWithProducts>,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onclickRow: (PostSaleWithProducts) -> Unit,
    onClickUpdate: () -> Unit,
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
                        title = "Ventas pendientes",
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
            ){
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
                        items(data) { sale ->
                            CardPenndingSaleCmp(
                                modifier = Modifier.clickable {
                                    onclickRow(sale)
                                },
                               data = sale,
                                onClick = { onclickRow(it) }
                                ,orderStatus = CustomEnums.OrderStatus.PENDIENTE
                            )
                            HorizontalDivider(thickness = PADDING_4, color = Color.Transparent, )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            GlassButton(
                icon = Icons.Filled.ArrowCircleUp,
                onClick = { onClickUpdate() },
                width = 60.dp,
                height = 60.dp
            )
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun PenndingSalesViewPreview(){
    PenndingSalesView(
        data = arrayListOf(),
        onClickBack = {  },
        onClickMenu = {  },
        onclickRow = {  },
        onClickUpdate = {  }
    )
}

