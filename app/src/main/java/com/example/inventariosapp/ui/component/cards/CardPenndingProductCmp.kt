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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import java.util.Locale

@Composable
fun CardPenndingProductCmp(
    producto: String,
    quantity: String,
    sellPrice: String,
    backgroundColor: Color,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_4)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
            ,
        ) {
            TextCmp(
                text = "${producto}",
                modifier = Modifier.padding(PADDING_8),
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(PADDING_8),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
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
                    modifier = Modifier,
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
                    modifier = Modifier,
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
                    val totalValue = (sellPrice.toDoubleOrNull() ?: 0.0) * (quantity.toDoubleOrNull() ?: 0.0)
                    val totalFormatted = String.format(
                        Locale.US,
                        "%.2f",
                        totalValue
                    )
                    TextCmp(
                        text = "${totalFormatted}",
                        modifier = Modifier,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        maxLine = 1
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPenndingProductCmpPreview() {
    CardPenndingProductCmp(
        backgroundColor = Color.Transparent,
        producto = "ACEITE PARA MOTO POWER RIDE 2T 10/300ml",
        quantity = "6",
        sellPrice = "60.00",
    )
}