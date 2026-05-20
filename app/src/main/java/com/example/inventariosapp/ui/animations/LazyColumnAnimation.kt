package com.example.inventariosapp.ui.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val PremiumEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

@Composable
fun <T> AnimatedLazyColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    animationDurationMillis: Int = 500,
    initialOffsetX: Dp = (-48).dp,
    @Suppress("UNUSED_PARAMETER") initialBlurRadius: Dp = 12.dp,
    staggerEnabled: Boolean = true,
    animateOnScroll: Boolean = true,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
    ) {
        itemsIndexed(
            items = items,
            key = if (key != null) { _, item -> key(item) } else null,
        ) { index, item ->
            AnimatedLazyItem(
                index = index,
                listState = listState,
                animationDurationMillis = animationDurationMillis,
                initialOffsetX = initialOffsetX,
                staggerEnabled = staggerEnabled,
                animateOnScroll = animateOnScroll,
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun AnimatedLazyItem(
    index: Int,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    animationDurationMillis: Int = 500,
    initialOffsetX: Dp = (-48).dp,
    staggerEnabled: Boolean = true,
    animateOnScroll: Boolean = true,
    content: @Composable () -> Unit,
) {
    val offsetX = remember { Animatable(initialOffsetX.value) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.96f) }
    var hasAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Suspend until this exact item index enters the viewport
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .filter { visible -> visible.any { it.index == index } }
            .first()

        if (hasAnimated) return@LaunchedEffect
        hasAnimated = true

        val visibleCount = listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1)
        val isScrollItem = index >= visibleCount

        // Snap immediately when scroll animation is disabled
        if (isScrollItem && !animateOnScroll) {
            offsetX.snapTo(0f)
            alpha.snapTo(1f)
            scale.snapTo(1f)
            return@LaunchedEffect
        }

        val perItemDuration = (animationDurationMillis / visibleCount).coerceIn(120, 500)
        val staggerDelay = if (staggerEnabled && !isScrollItem) {
            (index * (animationDurationMillis / visibleCount.toFloat() * 0.55f)).toLong()
        } else {
            0L
        }

        if (staggerDelay > 0L) delay(staggerDelay)

        val animSpec = tween<Float>(durationMillis = perItemDuration, easing = PremiumEasing)
        launch { offsetX.animateTo(0f, animSpec) }
        launch { alpha.animateTo(1f, animSpec) }
        launch { scale.animateTo(1f, animSpec) }
    }

    Box(
        modifier = modifier.graphicsLayer {
            translationX = offsetX.value.dp.toPx()
            this.alpha = alpha.value
            scaleX = scale.value
            scaleY = scale.value
        },
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedLazyColumnPreview() {
    val sampleItems = List(20) { index -> "Item ${index + 1} — Premium animated list entry" }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AnimatedLazyColumn(
                items = sampleItems,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                animationDurationMillis = 500,
                initialOffsetX = (-48).dp,
                initialBlurRadius = 12.dp,
                staggerEnabled = true,
                animateOnScroll = true,
                key = { it },
            ) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}