package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import java.util.Locale

@Composable
fun CardSellProductCmp(
    producto: String,
    quantity: String,
    sellPrice: String,
    backgroundColor: Color,
    onClickDelete: () -> Unit,
    //onClickPrint: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(PADDING_4),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
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
            val (product, cantidad, precioVenta, total, btnDelete, btnPrint) = createRefs()
            TextCmp(
                text = "${producto}",
                modifier = Modifier.constrainAs(product){
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                    end.linkTo(parent.end, PADDING_8)
                },
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            Row(
                modifier = Modifier
                    .constrainAs(cantidad){
                        top.linkTo(product.bottom, PADDING_16)
                        start.linkTo(parent.start, PADDING_8)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextCmp(
                    text = "Cantidad:",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
                TextCmp(
                    text = "$quantity",
                    modifier = Modifier,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
            }
            Row(
                modifier = Modifier
                    .constrainAs(precioVenta){
                        top.linkTo(product.bottom, PADDING_16)
                        start.linkTo(cantidad.end)
                        end.linkTo(total.start)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                TextCmp(
                    text = "Precio:",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
                TextCmp(
                    text = "$sellPrice",
                    modifier = Modifier,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
            }
            Row(
                modifier = Modifier
                    .constrainAs(total){
                        top.linkTo(product.bottom, PADDING_16)
                        end.linkTo(btnDelete.start, PADDING_8)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                TextCmp(
                    text = "Total:",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
                val total = String.format(
                    Locale.US,
                    "%.2f",
                    sellPrice.toDouble() * quantity.toDouble()
                ).toDouble()
                TextCmp(
                    text = "${total}",
                    modifier = Modifier,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 1
                )
            }
            Card(
                modifier = Modifier.constrainAs(btnDelete){
                    top.linkTo(product.bottom, PADDING_16)
                    end.linkTo(parent.end, PADDING_8)
                    bottom.linkTo(parent.bottom, PADDING_8)

                },
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ){
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onClickDelete() }
                )
            }
            /*
            Card(
                modifier = Modifier
                    .constrainAs(btnPrint){
                        top.linkTo(product.bottom, PADDING_16)
                        end.linkTo(parent.end, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)

                },
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_printer),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable{ onClickPrint() }
                )
            }

             */
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardSellProductCmpPreview(){
    CardSellProductCmp(

        backgroundColor = Color.Transparent,
        producto = "ACEITE PARA MOTO POWER RIDE 2T 10/300ml",
        quantity = "6",
        sellPrice = "60.00",
        //onClickPrint = {},
        onClickDelete = {}
    )
}