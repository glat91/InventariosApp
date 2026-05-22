package com.example.inventariosapp.ui.component.cards

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.model.payment.NewPayModel
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.util.CustomEnums

@Composable
fun CardPenndingPayCmp(
    modifier: Modifier = Modifier,
    data: NewPayModel,
    status: CustomEnums.StatusType,
    onClick: (NewPayModel) -> Unit,
) {
    val isError = status == CustomEnums.StatusType.ERROR

    val bgColor = if (isError) Color(0xFFFFF5F5) else Color(0xFFF9F9F9)
    val accentColor = if (isError) Color(0xFFE24B4A) else Color(0xFFBDBDBD)
    val badgeLabel = if (isError) "Error" else "Pendiente"
    val badgeBg = if (isError) Color(0xFFE24B4A).copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.07f)
    val badgeTextColor = if (isError) Color(0xFFE24B4A) else Color.Black.copy(alpha = 0.5f)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .scale(scale)
            .shadow(1.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.06f))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(0.5.dp, Color.Black.copy(alpha = 0.09f), RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick(data) }
    ) {
        // Barra de acento superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(accentColor)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1A1A)
                            )
                        ) { append("Venta ") }
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Normal,
                            color = Color.Black.copy(0.45f)))
                        { append("#${data.ventaId}") }
                    },
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentColor))
                    Text(
                        text = badgeLabel.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.06.em,
                        color = badgeTextColor
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.07f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaItem(
                    label = "Monto",
                    value = "$${data.montoPago}",
                    modifier = Modifier.weight(1f)
                )
                MetaItem(
                    label = "Fecha",
                    value = data.fecha,
                    modifier = Modifier.weight(1f)
                )
            }

            if (!data.observaciones.isNullOrBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    TextCmp(
                        text = "OBSERVACIONES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.07.em,
                        color = Color.Black.copy(alpha = 0.35f)
                    )
                    TextCmp(
                        text = data.observaciones,
                        fontSize = 13.sp,
                        color = Color.Black.copy(alpha = 0.55f),
                        maxLine = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaItem(label: String, value: String, modifier: Modifier = Modifier) {
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            maxLine = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CardPenndingPayErrorPreview() {
    CardPenndingPayCmp(
        data = NewPayModel(ventaId = 12345, montoPago = 2.00, fecha = "20-20-2025", observaciones = "Pago rechazado por el servidor"),
        status = CustomEnums.StatusType.ERROR,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CardPenndingPayPendingPreview() {
    CardPenndingPayCmp(
        data = NewPayModel(ventaId = 67890, montoPago = 850.00, fecha = "15-05-2026", observaciones = "En espera de sincronización"),
        status = CustomEnums.StatusType.PENDING,
        onClick = {}
    )
}