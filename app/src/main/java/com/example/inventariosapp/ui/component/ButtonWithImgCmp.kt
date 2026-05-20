package com.example.inventariosapp.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun ButtonWithImgCmp(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    backgroundColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()

    // Ripple offset animado
    var rippleOffset by remember { mutableStateOf(Offset.Zero) }
    var rippleVisible by remember { mutableStateOf(false) }
    val rippleRadius = remember { Animatable(0f) }
    val rippleAlpha = remember { Animatable(0f) }

    // Escala al presionar
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "scale"
    )

    // Elevación animada
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 6.dp,
        animationSpec = tween(120),
        label = "elevation"
    )

    // Shimmer offset en hover (requiere pointer events en Desktop; en mobile se dispara sólo)
    val shimmerAnim = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by shimmerAnim.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "shimmerX"
    )

    val contentColor = remember(backgroundColor) {
        // Elige blanco o negro según luminancia
        val lum = 0.2126f * backgroundColor.red +
                0.7152f * backgroundColor.green +
                0.0722f * backgroundColor.blue
        if (lum > 0.5f) Color(0xFF1A1A1A) else Color.White
    }

    val disabledBg = Color(0xFFBDBDBD)
    val disabledContent = Color(0xFF757575)
    val activeBg = backgroundColor

    Box(
        modifier = modifier
            .height(44.dp)
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(12.dp),
                ambientColor = activeBg.copy(alpha = 0.35f),
                spotColor = activeBg.copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (enable) activeBg else disabledBg
            )
            // Shimmer layer
            .drawWithContent {
                drawContent()
                if (enable) {
                    val shimmerBrush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerX * size.width, 0f),
                        end = Offset((shimmerX + 0.5f) * size.width, size.height)
                    )
                    drawRect(shimmerBrush)
                }
                // Ripple manual
                if (rippleVisible) {
                    drawCircle(
                        color = Color.White.copy(alpha = rippleAlpha.value),
                        radius = rippleRadius.value,
                        center = rippleOffset
                    )
                }
            }
            .border(
                width = 1.dp,
                brush = if (enable)
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.45f),
                            Color.Transparent,
                            activeBg.copy(alpha = 0.3f)
                        )
                    )
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enable
            ) {
                scope.launch {
                    // Ripple animation
                    rippleOffset = Offset(Float.NaN, Float.NaN) // centro
                    rippleVisible = true
                    rippleAlpha.snapTo(0.35f)
                    rippleRadius.snapTo(0f)
                    launch { rippleRadius.animateTo(200f, tween(400, easing = FastOutSlowInEasing)) }
                    launch {
                        rippleAlpha.animateTo(0f, tween(380, easing = FastOutLinearInEasing))
                        rippleVisible = false
                    }
                    onClick()
                }
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (enable) contentColor else disabledContent,
                letterSpacing = 0.3.sp
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (enable) contentColor.copy(alpha = 0.85f) else disabledContent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonCmpPreview2(){
    ButtonWithImgCmp(text="Guardar", backgroundColor=Color(0xFFF5C518), icon= Icons.Default.Save, onClick={})
}