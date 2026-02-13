package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Cancel

@Composable
fun CardInventoryCmp(
    producto: String,
    departamento: String,
    costo: String,
    precio1: String,
    precio2: String? = null,
    precio3: String? = null,
    precio4: String? = null,
    backgroundColor: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(PADDING_4),
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        shape = RoundedCornerShape(5.dp),
        //border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_4)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
            ,
        ) {
            val (product, dep, price1) = createRefs()
            TextCmp(
                text = "${producto}",
                modifier = Modifier.constrainAs(product){
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                    end.linkTo(parent.end, PADDING_8)
                },
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(price1){
                    top.linkTo(product.bottom, PADDING_4)
                    start.linkTo(parent.start, PADDING_8)
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextCmp(
                        text = "Precio 1: $precio1",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        maxLine = 1
                    )
                    if (!precio2.isNullOrBlank()){
                        TextCmp(
                            text = "Precio 2: $precio2",
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            maxLine = 1
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!precio3.isNullOrBlank()){
                        TextCmp(
                            text = "Precio 3: $precio3",
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            maxLine = 1
                        )
                    }
                    if (!precio4.isNullOrBlank()){
                        TextCmp(
                            text = "Precio 4: $precio4",
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            maxLine = 1
                        )
                    }
                }
            }


            Row(modifier = Modifier.constrainAs(dep){
                top.linkTo(price1.bottom, PADDING_8)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                TextCmp(
                    text = "Departamento: $departamento",
                    modifier = Modifier.padding(PADDING_8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
                TextCmp(
                    text = "Costo: $costo",
                    modifier = Modifier.padding(PADDING_8),
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
fun CardProductCmpPReview(){
    CardInventoryCmp(
        producto = "HARINA CUETARA HOTCAKES TRADICIONALES 10/800gr",
        backgroundColor = UI_Backround_Btn_Cancel,
        departamento ="Despensa",
        costo = "63.00",
        precio1 = "$12,285.01",
        precio2 = "$12,274.55",
        precio3 = "$12,274.55",
        precio4 = "$12,274.55"
    )
}