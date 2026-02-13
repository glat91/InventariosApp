package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.model.payment.NewPayModel
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.util.CustomEnums

@Composable
fun CardPenndingPayCmp(
    modifier: Modifier,
    data: NewPayModel,
    status: CustomEnums.StatusType,
    onClick: (NewPayModel) -> Unit,
) {
    val colorBackground = when(status){
        CustomEnums.StatusType.ERROR -> { Color.Red }
        CustomEnums.StatusType.PENDING -> { Color.LightGray }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(colorBackground)
            .padding(PADDING_4)
            .clickable { onClick(data) }
        ,
        colors = CardColors(
            containerColor = colorBackground,
            contentColor = colorBackground,
            disabledContainerColor = colorBackground,
            disabledContentColor = colorBackground
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(PADDING_8),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextCmp(
                        text = "Venta ID: ${data.ventaId}",
                        modifier = Modifier.padding(top = PADDING_4),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        maxLine = 2
                    )
                    TextCmp(
                        text = "Monto: $${data.montoPago}",
                        modifier = Modifier.padding(top = PADDING_4),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        maxLine = 2
                    )
                }
                TextCmp(
                    text = "Fecha: ${data.fecha}",
                    modifier = Modifier.padding(top = PADDING_4).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLine = 2
                )
                TextCmp(
                    text = "Observaciones: ${data.observaciones}",
                    modifier = Modifier.padding(top = PADDING_4).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    maxLine = 2
                )
            }
        }
    }
}

@Preview
@Composable
fun CardPenndingPayCmpPreview(){
    CardPenndingPayCmp(
        modifier = Modifier,
        data = NewPayModel(
            ventaId = 12345,
            montoPago = 2.00,
            fecha = "20-20-2025"
        ),
        status = CustomEnums.StatusType.ERROR,
        onClick = {},
    )
}