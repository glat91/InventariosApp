package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@Composable
fun CardInventoryCmp(
    producto: String,
    departamento: String,
    costo: String,
    precio1: String,
    precio2: String? = null,
    precio3: String? = null,
    precio4: String? = null,
) {
    val prices = listOf(
        "Precio 1" to precio1,
        "Precio 2" to precio2,
        "Precio 3" to precio3,
        "Precio 4" to precio4,
    ).filter { !it.second.isNullOrBlank() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(14.dp), ambientColor = Color.Black.copy(0.05f))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(0.5.dp, Color.Black.copy(alpha = 0.09f), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextCmp(
                text = producto,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                maxLine = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))

            val columns = 2
            prices.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { (label, value) ->
                        PriceMetaItem(
                            label = label,
                            value = "$$value",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size < columns) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                TextCmp(
                    text = "COSTO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.07.em,
                    color = Color.Black.copy(0.35f)
                )
                TextCmp(
                    text = "$$costo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
            }
            TextCmp(
                text = departamento,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.04.em,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF3F3F3))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun PriceMetaItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        TextCmp(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.07.em,
            color = Color.Black.copy(0.35f)
        )
        TextCmp(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            maxLine = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CardInventoryCmpPreview() {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CardInventoryCmp(
            producto = "HARINA CUETARA HOTCAKES TRADICIONALES 10/800gr",
            departamento = "Despensa",
            costo = "63.00",
            precio1 = "12,285.01",
            precio2 = "12,274.55",
            precio3 = "12,274.55",
            precio4 = "12,274.55"
        )
        CardInventoryCmp(
            producto = "ACEITE VEGETAL PRIMAVERA 1LT",
            departamento = "Abarrotes",
            costo = "28.00",
            precio1 = "38.50"
        )
    }
}