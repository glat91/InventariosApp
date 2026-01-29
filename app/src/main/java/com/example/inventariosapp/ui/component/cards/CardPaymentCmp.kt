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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.inventariosapp.ui.theme.PADDING_4

@Composable
fun CardPaymentCmp(
    modifier: Modifier,
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
           // val (nombre, total, pagado, pagar, fecha, )
        }
    }
}