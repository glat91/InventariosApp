package com.example.inventariosapp.ui.view.user_payments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.R
import com.example.inventariosapp.ui.component.HeaderCmp
import com.example.inventariosapp.ui.component.cards.CardPenndingPayCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Yellow
import com.example.inventariosapp.ui.theme.UI_Backround_Top
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.util.CustomEnums

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPaymentsView(
    data: List<NewPayModel>,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickUpdate: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(0.dp).background(UI_Backround_Top),
                title = {
                    HeaderCmp(
                        title = "Ventas pendientes",
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
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.padding(PADDING_8),
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(data) { pay ->
                            HorizontalDivider(thickness = PADDING_4, color = Color.Transparent, )
                            CardPenndingPayCmp(
                                modifier = Modifier,
                                data = pay,
                                status = CustomEnums.StatusType.PENDING,
                                onClick = {},
                            )
                            HorizontalDivider(thickness = PADDING_4, color = Color.Transparent, )
                            HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            Card(
                modifier = Modifier.clickable{ onClickUpdate() },
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardColors(
                    containerColor = UI_Backround_Btn_Yellow,
                    contentColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = "Logo de la app",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    )
}

@Preview
@Composable
fun UserPaymentsViewPreview(){
    UserPaymentsView(
        data = arrayListOf(),
        onClickBack = {},
        onClickMenu = {},
        onClickUpdate = {}
    )
}
