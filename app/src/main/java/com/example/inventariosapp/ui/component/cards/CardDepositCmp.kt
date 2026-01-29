package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.R
import com.example.inventariosapp.ui.theme.PADDING_16
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_48
import com.example.inventariosapp.ui.theme.PADDING_8

@Composable
fun CardDepositCmp(
    date: String,
    totalAmount: String,
    observations: String,
    backgroundColor: Color,
    onClickDelete: () -> Unit,
    onClickPrint: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(PADDING_4),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
    ){
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth().padding(PADDING_8).background(backgroundColor)
        ) {
            val (fecha, total, name, btn1, btn2) = createRefs()
            TextCmp(
                text = "Fecha: ${date.take(10)}",
                modifier = Modifier.constrainAs(fecha) {
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            TextCmp(
                text = "Monto: $$totalAmount",
                modifier = Modifier.constrainAs(total) {
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(fecha.end, PADDING_16)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )

            TextCmp(
                text = observations,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(fecha.bottom, PADDING_16)
                    start.linkTo(parent.start, PADDING_8)
                    bottom.linkTo(parent.bottom, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )

            Card(
                modifier = Modifier
                    .constrainAs(btn1) {
                        end.linkTo(parent.end, PADDING_48)
                        bottom.linkTo(parent.bottom, PADDING_8)

                    }
                    .width(35.dp)
                    .clickable { onClickDelete() },
                //shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Borrar Pago",
                    )
                }
            }
            Card(
                modifier = Modifier
                    .constrainAs(btn2) {
                        start.linkTo(btn1.end, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)
                    }
                    .width(35.dp)
                    .clickable { onClickPrint() },
                //shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_printer),
                        contentDescription = "Imprimir",
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardCmpPreview(){
    CardDepositCmp(
        date = "12-12-2025",
        totalAmount = "999,999.99",
        observations = "Albertano Fulanito Garzano Herrerenza",
        backgroundColor = Color.Gray,
        onClickDelete = {},
        onClickPrint = {}
    )
}