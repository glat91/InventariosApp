package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@Composable
fun CardSaleCmp(
    modifier: Modifier = Modifier,
    nameClient: String,
    montoPagado: String,
    montoPagar: String,
    saleDate: String,
    payLimitDate: String,
    folio: String,
) {
    val tieneAdeudo = (montoPagar.replace(",", "").toDoubleOrNull() ?: 0.0) > 0.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .shadow(1.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.05f))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(0.5.dp, Color.Black.copy(alpha = 0.09f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                TextCmp(
                    text = nameClient,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A),
                    maxLine = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                TextCmp(
                    text = "# $folio",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.06.em,
                    color = Color.Black.copy(alpha = 0.38f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF3F3F3))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateChip(icon = Icons.Default.ShoppingCart, label = "Venta", date = saleDate)
                DateChip(icon = Icons.Default.CalendarMonth, label = "Límite", date = payLimitDate)
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))

        // — Parte inferior: montos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoneyGroup(label = "Pagado", value = "$$montoPagado", highlight = false)
            Box(
                modifier = Modifier
                    .width(0.5.dp)
                    .height(32.dp)
                    .background(Color.Black.copy(alpha = 0.1f))
            )
            MoneyGroup(label = "Resto", value = "$$montoPagar", highlight = tieneAdeudo)
        }
    }
}

@Composable
private fun DateChip(icon: ImageVector, label: String, date: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.Black.copy(0.3f), modifier = Modifier.size(12.dp))
        TextCmp(text = label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.05.em, color = Color.Black.copy(0.35f))
        TextCmp(text = date, fontSize = 12.sp, color = Color(0xFF1A1A1A))
    }
}

@Composable
private fun MoneyGroup(label: String, value: String, highlight: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        TextCmp(text = label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.07.em, color = Color.Black.copy(0.35f))
        TextCmp(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (highlight) Color(0xFFE24B4A) else Color(0xFF1A1A1A)
        )
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
    )
}