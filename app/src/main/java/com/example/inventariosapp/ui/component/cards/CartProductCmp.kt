package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Cancel

@Composable
fun CardProductCmp(
    modifier: Modifier,
    product: String,
    measureUnit: String,
    backgroundColor: Color,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(PADDING_4),
        colors = CardColors(
            containerColor =backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
    ){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_4)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
            ,
        ){
            val (producto, medida, maxStock, minStock, total) = createRefs()
            TextCmp(
                text = "$product",
                modifier = Modifier.fillMaxWidth().constrainAs(producto){
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                    end.linkTo(parent.end, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth().constrainAs(medida){
                    top.linkTo(producto.bottom, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                    bottom.linkTo(parent.bottom, PADDING_8)
                    end.linkTo(parent.end, PADDING_8)
                },
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextCmp(
                    text = "Medida: $measureUnit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartProductCmpPreview(){
    CardProductCmp(
        modifier = Modifier,
        backgroundColor = UI_Backround_Btn_Cancel,
        product = "DEO AE NIVEA FRESH OCEAN CABALLERO 4/150ML",
        measureUnit = "PAQUETE",
    )
}