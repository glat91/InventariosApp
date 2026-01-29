package com.example.inventariosapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.R
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8

@Composable
fun MenuItemCmp(
    image: Int,
    txtOpcion: String,
    onClickOpc: () -> Unit
) {
    Card(
        modifier = Modifier.padding(top = PADDING_16, bottom = PADDING_16),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickOpc() }
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Logo de la app",
                modifier = Modifier
                    .padding(end = PADDING_8, start = PADDING_4)
                    .size(35.dp)
                ,
                contentScale = ContentScale.Fit
            )

            TextCmp(
                text = txtOpcion,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemCmpPreview(){
    MenuItemCmp(
        image = R.drawable.ic_bar_chart,
        txtOpcion = "Opcion de menu",
        onClickOpc = { }
    )
}