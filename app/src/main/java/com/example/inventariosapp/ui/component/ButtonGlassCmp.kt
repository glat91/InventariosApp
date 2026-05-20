package com.example.inventariosapp.ui.component
import android.graphics.drawable.Icon
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.example.inventariosapp.R
import kotlin.math.*
import kotlin.random.Random

// ── State ────────────────────────────────────────────────────────────────────

enum class GlassState { IDLE, PRESSING, SHATTERED, REFORMING }

data class Shard(
    val path: List<Offset>,
    val velocity: Offset,
    val angularVelocity: Float,
    val opacity: Float = 1f
)

// ── Main Component ────────────────────────────────────────────────────────────

@Composable
fun GlassButton(
    label: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 220.dp,
    height: Dp = 56.dp,
) {
    var glassState by remember { mutableStateOf(GlassState.IDLE) }
    var tapOffset by remember { mutableStateOf(Offset.Zero) }
    var shards by remember { mutableStateOf<List<Shard>>(emptyList()) }

    val density = LocalDensity.current
    val widthPx = with(density) { width.toPx() }
    val heightPx = with(density) { height.toPx() }

    // Press scale
    val pressScale by animateFloatAsState(
        targetValue = if (glassState == GlassState.PRESSING) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pressScale"
    )

    // Crack progress
    val crackProgress by animateFloatAsState(
        targetValue = if (glassState == GlassState.SHATTERED || glassState == GlassState.REFORMING) 1f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing),
        label = "crackProgress"
    )

    // Shatter progress (shards flying)
    val shatterProgress by animateFloatAsState(
        targetValue = if (glassState == GlassState.SHATTERED) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "shatterProgress",
        finishedListener = {
            if (glassState == GlassState.SHATTERED) glassState = GlassState.REFORMING
        }
    )

    // Reform progress
    val reformProgress by animateFloatAsState(
        targetValue = if (glassState == GlassState.REFORMING) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "reformProgress",
        finishedListener = {
            if (glassState == GlassState.REFORMING) glassState = GlassState.IDLE
        }
    )

    // Shimmer
    val shimmerOffset by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = -widthPx,
        targetValue = widthPx * 2,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing)),
        label = "shimmerOffset"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width, height)
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        if (glassState == GlassState.IDLE) {
                            glassState = GlassState.PRESSING
                            val pressed = tryAwaitRelease()
                            if (pressed) {
                                tapOffset = offset
                                shards = generateShards(offset, widthPx, heightPx)
                                glassState = GlassState.SHATTERED
                                onClick()
                            } else {
                                glassState = GlassState.IDLE
                            }
                        }
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGlassBase(shimmerOffset, widthPx, heightPx)

            if (crackProgress > 0f && glassState != GlassState.IDLE) {
                drawCracks(tapOffset, crackProgress, widthPx, heightPx)
            }

            if (shatterProgress > 0f) {
                drawShards(shards, shatterProgress)
            }

            if (reformProgress > 0f && glassState == GlassState.REFORMING) {
                drawReform(reformProgress, widthPx, heightPx)
            }
        }

        val textAlpha = when {
            shatterProgress > 0.2f -> 1f - ((shatterProgress - 0.2f) / 0.8f).coerceIn(0f, 1f)
            reformProgress > 0.5f -> (reformProgress - 0.5f) / 0.5f
            glassState == GlassState.REFORMING && reformProgress <= 0.5f -> 0f
            else -> 1f
        }
        if (icon != null){
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = Color.White.copy(alpha = textAlpha),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {  }
            )
        }
        if (label != null){
            Text(
                text = label,
                color = Color.White.copy(alpha = textAlpha),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.2.sp,
                modifier = Modifier.graphicsLayer { alpha = textAlpha }
            )
        }



    }
}

// ── Drawing ───────────────────────────────────────────────────────────────────

fun DrawScope.drawGlassBase(shimmerOffset: Float, w: Float, h: Float) {
    // Base glass fill
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF6EE7F7).copy(alpha = 0.18f),
                Color(0xFF818CF8).copy(alpha = 0.22f),
                Color(0xFF34D399).copy(alpha = 0.12f),
            ),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )

    // Top highlight
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
            startY = 0f, endY = h * 0.45f
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )

    // Shimmer sweep
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.18f),
                Color.Transparent
            ),
            start = Offset(shimmerOffset - 60f, 0f),
            end = Offset(shimmerOffset + 60f, h)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )

    // Border
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.55f),
                Color(0xFF818CF8).copy(alpha = 0.4f),
                Color.White.copy(alpha = 0.15f)
            ),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
        style = Stroke(width = 1.2.dp.toPx())
    )
}

fun DrawScope.drawCracks(impact: Offset, progress: Float, w: Float, h: Float) {
    val paint = Paint().apply {
        color = Color.White.copy(alpha = 0.7f * progress)
        strokeWidth = 1.dp.toPx()
        strokeCap = StrokeCap.Round
    }

    val rng = Random(42)
    val armCount = 8
    repeat(armCount) { i ->
        val angle = (i.toFloat() / armCount) * 2 * PI.toFloat() + rng.nextFloat() * 0.4f
        val length = (rng.nextFloat() * 0.35f + 0.25f) * minOf(w, h) * progress
        val endX = impact.x + cos(angle) * length
        val endY = impact.y + sin(angle) * length

        drawLine(
            color = Color.White.copy(alpha = 0.65f * progress),
            start = impact,
            end = Offset(endX, endY),
            strokeWidth = 0.8.dp.toPx()
        )

        // sub-cracks
        val branches = rng.nextInt(1, 3)
        repeat(branches) {
            val branchStart = Offset(
                impact.x + cos(angle) * length * (0.3f + rng.nextFloat() * 0.5f),
                impact.y + sin(angle) * length * (0.3f + rng.nextFloat() * 0.5f)
            )
            val branchAngle = angle + (rng.nextFloat() - 0.5f) * PI.toFloat() * 0.6f
            val branchLen = length * (0.2f + rng.nextFloat() * 0.25f)
            drawLine(
                color = Color.White.copy(alpha = 0.45f * progress),
                start = branchStart,
                end = Offset(branchStart.x + cos(branchAngle) * branchLen, branchStart.y + sin(branchAngle) * branchLen),
                strokeWidth = 0.5.dp.toPx()
            )
        }
    }
}

fun DrawScope.drawShards(shards: List<Shard>, progress: Float) {
    shards.forEach { shard ->
        val tx = shard.velocity.x * progress * 180f
        val ty = shard.velocity.y * progress * 180f
        val rotation = shard.angularVelocity * progress * 120f
        val alpha = (1f - progress * 1.2f).coerceIn(0f, 1f)

        val cx = shard.path.map { it.x }.average().toFloat()
        val cy = shard.path.map { it.y }.average().toFloat()

        drawContext.canvas.save()
        drawContext.canvas.translate(tx, ty)
        drawContext.canvas.rotate(rotation, cx, cy)

        val path = Path().apply {
            moveTo(shard.path[0].x, shard.path[0].y)
            shard.path.drop(1).forEach { lineTo(it.x, it.y) }
            close()
        }

        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF6EE7F7).copy(alpha = alpha * 0.55f),
                    Color(0xFF818CF8).copy(alpha = alpha * 0.45f)
                )
            )
        )
        drawPath(
            path = path,
            color = Color.White.copy(alpha = alpha * 0.8f),
            style = Stroke(width = 0.7.dp.toPx())
        )

        drawContext.canvas.restore()
    }
}

fun DrawScope.drawReform(progress: Float, w: Float, h: Float) {
    val alpha = (progress * 2f - 1f).coerceIn(0f, 1f)
    drawRoundRect(
        color = Color(0xFF6EE7F7).copy(alpha = alpha * 0.25f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = alpha * 0.3f), Color.Transparent)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )
}

// ── Shard Generation ──────────────────────────────────────────────────────────

fun generateShards(impact: Offset, w: Float, h: Float): List<Shard> {
    val rng = Random(System.currentTimeMillis())
    val corners = listOf(Offset(0f, 0f), Offset(w, 0f), Offset(w, h), Offset(0f, h))
    val edgeMids = listOf(Offset(w / 2, 0f), Offset(w, h / 2), Offset(w / 2, h), Offset(0f, h / 2))
    val extra = List(6) { Offset(rng.nextFloat() * w, rng.nextFloat() * h) }
    val points = (corners + edgeMids + extra + impact).distinctBy { "${it.x.toInt()},${it.y.toInt()}" }

    return points.mapIndexed { idx, pt ->
        val neighbors = points.sortedBy { hypot((it.x - pt.x), (it.y - pt.y)) }.drop(1).take(2)
        val cx = (pt.x + neighbors[0].x + neighbors[1].x) / 3f
        val cy = (pt.y + neighbors[0].y + neighbors[1].y) / 3f
        val dx = cx - impact.x
        val dy = cy - impact.y
        val dist = hypot(dx, dy).coerceAtLeast(1f)
        val speed = (1f - (dist / hypot(w, h)).coerceIn(0f, 1f)) * 1.6f + 0.4f

        Shard(
            path = listOf(pt, neighbors[0], neighbors[1]),
            velocity = Offset(dx / dist * speed, dy / dist * speed),
            angularVelocity = (rng.nextFloat() - 0.5f) * 2.5f
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun GlassButtonPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            GlassButton(label = "CONFIRM", onClick = {})
            GlassButton(label = "SHATTER ME", onClick = {}, width = 180.dp, height = 48.dp)
            GlassButton(icon = Icons.Filled.Delete, onClick = {}, width = 50.dp, height = 48.dp)
        }
    }
}