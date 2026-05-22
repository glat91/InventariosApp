package com.example.inventariosapp.ui.component.cards

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import java.util.Locale

@Composable
fun CardSellProductCmp(
    producto: String,
    quantity: String,
    sellPrice: String,
    deleteIcon: Boolean = true,
    onClickDelete: () -> Unit,
) {
    val totalValue = (sellPrice.toDoubleOrNull() ?: 0.0) * (quantity.toDoubleOrNull() ?: 0.0)
    val totalFormatted = String.format(Locale.US, "%.2f", totalValue)

    val deletePressed = remember { mutableStateOf(false) }
    val deleteScale by animateFloatAsState(
        targetValue = if (deletePressed.value) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "deleteScale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color.Black.copy(alpha = 0.05f))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(0.5.dp, Color.Black.copy(alpha = 0.09f), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Nombre + botón delete
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            TextCmp(
                text = producto,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                maxLine = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            if (deleteIcon){
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .scale(deleteScale)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF7F7F7))
                        .border(1.dp, Color.Black.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onClickDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.07f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            MetaGroup(label = "Cant.", value = quantity)
            MetaGroup(label = "Precio", value = "$$sellPrice")
            MetaGroup(label = "Total", value = "$$totalFormatted", valueSize = 16.sp, valueBold = true)
        }
    }
}

@Composable
private fun MetaGroup(
    label: String,
    value: String,
    valueSize: TextUnit = 14.sp,
    valueBold: Boolean = false
) {
    Column(horizontalAlignment = Alignment.Start) {
        TextCmp(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.07.em,
            color = Color.Black.copy(alpha = 0.35f)
        )
        Spacer(Modifier.height(2.dp))
        TextCmp(
            text = value,
            fontSize = valueSize,
            fontWeight = if (valueBold) FontWeight.SemiBold else FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            maxLine = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardSellProductCmpPreview(){
    CardSellProductCmp(
        producto = "ACEITE PARA MOTO POWER RIDE 2T 10/300ml",
        quantity = "6",
        sellPrice = "60.00",
        deleteIcon = false,
        onClickDelete = {}
    )
}