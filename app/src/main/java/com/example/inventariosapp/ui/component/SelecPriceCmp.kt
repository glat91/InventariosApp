package com.example.inventariosapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8

@Composable
fun SelecPriceCmp(
    modifier: Modifier,
    precio: String,
    selected: MutableState<Boolean>
) {
    val colorBackground = if (selected.value) Color.Black else Color.White
    val colorLetter = if (selected.value) Color.White else Color.Black
    Card(
        modifier = modifier.padding(PADDING_4),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorBackground
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = .85f))
    ){
        Row(
            modifier = Modifier.padding(PADDING_8)
        ) {
            TextCmp(
                text = precio,
                modifier = Modifier,
                color = colorLetter,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif,
                maxLine = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelecPriceCmpPreview(){
    SelecPriceCmp(
        modifier = Modifier,
        precio ="23.00",
        selected = remember { mutableStateOf(true) }
    )
}