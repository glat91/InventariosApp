package com.example.inventariosapp.ui.component.cards

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.R

@Composable
fun CardDepositCmp(
    date: String,
    totalAmount: String,
    observations: String,
    onClickDelete: () -> Unit,
    onClickPrint: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(14.dp), ambientColor = Color.Black.copy(0.05f))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(0.5.dp, Color.Black.copy(alpha = 0.09f), RoundedCornerShape(14.dp))
    ) {
        // ── Cuerpo ───────────────────────────────────────
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DepositMetaItem(
                    label = "Fecha",
                    value = date.take(10)
                )
                DepositMetaItem(
                    label = "Monto",
                    value = "$$totalAmount",
                    valueSize = 16.sp,
                    bold = true,
                    align = Alignment.End
                )
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))

            if (observations.isNotBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    TextCmp(
                        text = "OBSERVACIONES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.07.em,
                        color = Color.Black.copy(alpha = 0.35f)
                    )
                    Text(
                        text = observations,
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.5f),
                        lineHeight = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // ── Footer — acciones ─────────────────────────────
        HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imprimir
            DepositActionButton(
                icon = painterResource(id = R.drawable.ic_printer),
                contentDescription = "Imprimir",
                tint = Color.Black.copy(alpha = 0.5f),
                bg = Color(0xFFF3F3F3),
                border = Color.Black.copy(alpha = 0.08f),
                onClick = onClickPrint
            )
            // Eliminar
            DepositActionButton(
                icon = null,
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFE24B4A),
                bg = Color(0xFFE24B4A).copy(alpha = 0.08f),
                border = Color(0xFFE24B4A).copy(alpha = 0.15f),
                onClick = onClickDelete
            )
        }
    }
}

@Composable
private fun DepositMetaItem(
    label: String,
    value: String,
    valueSize: TextUnit = 14.sp,
    bold: Boolean = false,
    align: Alignment.Horizontal = Alignment.Start
) {
    Column(horizontalAlignment = align) {
        TextCmp(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.07.em,
            color = Color.Black.copy(0.35f)
        )
        Spacer(Modifier.height(2.dp))
        TextCmp(
            text = value,
            fontSize = valueSize,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            maxLine = 1
        )
    }
}

@Composable
private fun DepositActionButton(
    icon: Painter? = null,
    imageVector: ImageVector? = null,
    contentDescription: String,
    tint: Color,
    bg: Color,
    border: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )
    Box(
        modifier = Modifier
            .size(32.dp)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when {
            imageVector != null -> Icon(
                imageVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            icon != null -> Icon(
                modifier = Modifier.size(16.dp),
                painter = icon,
                contentDescription = contentDescription,
                tint = tint,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardCmpPreview(){
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CardDepositCmp(
            date = "12-12-2025",
            totalAmount = "999,999.99",
            observations = "Albertano Fulanito Garzano Herrerenza",
            onClickDelete = {},
            onClickPrint = {}
        )
        CardDepositCmp(
            date = "05-03-2026",
            totalAmount = "1,500.00",
            observations = "Pago adelantado segundo trimestre",
            onClickDelete = {},
            onClickPrint = {}
        )
    }
}