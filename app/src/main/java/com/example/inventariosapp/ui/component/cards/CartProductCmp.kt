package com.example.inventariosapp.ui.component.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@Composable
fun CardProductCmp(
    modifier: Modifier = Modifier,
    product: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val bgColor by animateColorAsState(
        targetValue = when {
            isPressed -> Color(0xFFEDEDED)
            isSelected -> Color(0xFFF0F0F0)
            else -> Color.White
        },
        animationSpec = tween(130),
        label = "itemBg"
    )
    val checkAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(150),
        label = "check"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFF3F3F3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.35f),
                modifier = Modifier.size(15.dp)
            )
        }
        // Nombre
        TextCmp(
            text = product,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            lineHeight = 18.sp,
            maxLine = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        // Check seleccionado
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF1A1A1A),
            modifier = Modifier
                .size(16.dp)
                .alpha(checkAlpha)
        )
    }

}
@Preview(showBackground = false)
@Composable
fun CartProductCmpPreview(){
    CardProductCmp(
        modifier = Modifier,
        product = "DEO AE NIVEA FRESH OCEAN CABALLERO 4/150ML",
    )
}