package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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

@Composable
fun CardSaleCmp(
    modifier: Modifier,
    nameClient: String,
    montoPagado: String,
    montoPagar: String,
    saleDate: String,
    payLimitDate: String,
    folio: String,
    backgroundColor: Color,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(PADDING_4),
        colors = CardColors(
            containerColor = Color.White,
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
            val (nombre, fechaLimite, pagado, pagar, fechaVenta, f) = createRefs()

            TextCmp(
                text = "$nameClient",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(nombre){
                        top.linkTo(parent.top, PADDING_8)
                        start.linkTo(parent.start, PADDING_8)
                        end.linkTo(parent.end, PADDING_8)
                },
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )

            TextCmp(
                text = "$folio",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(f){
                        top.linkTo(nombre.bottom, PADDING_4)
                        start.linkTo(parent.start, PADDING_8)
                        end.linkTo(parent.end, PADDING_8)
                    },
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 1
            )

            TextCmp(
                text = "Fecha Limite: $payLimitDate",
                modifier = Modifier
                    .constrainAs(fechaLimite){
                        top.linkTo(nombre.bottom, PADDING_8)
                        start.linkTo(parent.start, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)
                    },
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 1
            )

            TextCmp(
                text = "Fecha Venta: $saleDate",
                modifier = Modifier
                    .constrainAs(fechaVenta){
                        top.linkTo(nombre.bottom, PADDING_8)
                        end.linkTo(parent.end, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)
                    },
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 1
            )

            TextCmp(
                text = "Pagado: $montoPagado",
                modifier = Modifier
                    .constrainAs(pagado){
                        top.linkTo(fechaLimite.bottom, PADDING_8)
                        start.linkTo(parent.start, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)
                    },
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 1
            )

            TextCmp(
                text = "Resto: $montoPagar",
                modifier = Modifier
                    .constrainAs(pagar){
                        top.linkTo(fechaVenta.bottom, PADDING_8)
                        end.linkTo(parent.end, PADDING_8)
                        bottom.linkTo(parent.bottom, PADDING_8)
                    },
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 1
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardSaleCmpPReview(){
    CardSaleCmp(
        modifier = Modifier,
        nameClient = "Cliente",
        payLimitDate = "20-06-2026",
        montoPagado = "999,999.00",
        montoPagar = "999,999.00",
        saleDate = "20-06-2025",
        folio = "524568",
        backgroundColor = Color.White
    )
}