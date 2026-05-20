package com.example.inventariosapp.ui.animations

import android.os.Build
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NamedNavArgument

// ─── Easing ──────────────────────────────────────────────────────────────────

private val PremiumEasing = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
private val DecelerateEasing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
private val AccelerateEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

// ─── Blur Modifier ───────────────────────────────────────────────────────────

/**
 * Applies a blur effect using RenderEffect on API 31+.
 * Falls back to a lightweight alpha-dimming simulation on older APIs.
 */
fun Modifier.blurCompat(radiusDp: Dp, fallbackAlpha: Float = 1f): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && radiusDp > 0.dp) {
        this.graphicsLayer {
            val radiusPx = radiusDp.toPx()
            renderEffect = android.graphics.RenderEffect
                .createBlurEffect(radiusPx, radiusPx, android.graphics.Shader.TileMode.CLAMP)
                .asComposeRenderEffect()
        }
    } else {
        // Pre-API 31 simulation: reduce alpha slightly to hint at blur without heavy ops
        this.graphicsLayer {
            alpha = fallbackAlpha
        }
    }
}

// ─── Transition Specs ────────────────────────────────────────────────────────

private fun enterTransition(durationMillis: Int): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = DecelerateEasing
        ),
        initialOffsetX = { fullWidth -> -fullWidth }
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = (durationMillis * 0.7f).toInt(),
            delayMillis = (durationMillis * 0.1f).toInt(),
            easing = PremiumEasing
        )
    )

private fun exitTransition(durationMillis: Int): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = AccelerateEasing
        ),
        targetOffsetX = { fullWidth -> fullWidth }
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = (durationMillis * 0.6f).toInt(),
            easing = AccelerateEasing
        )
    )

private fun popEnterTransition(durationMillis: Int): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = DecelerateEasing
        ),
        initialOffsetX = { fullWidth -> fullWidth }
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = (durationMillis * 0.7f).toInt(),
            delayMillis = (durationMillis * 0.1f).toInt(),
            easing = PremiumEasing
        )
    )

private fun popExitTransition(durationMillis: Int): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = AccelerateEasing
        ),
        targetOffsetX = { fullWidth -> -fullWidth }
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = (durationMillis * 0.6f).toInt(),
            easing = AccelerateEasing
        )
    )

// ─── animatedComposable ──────────────────────────────────────────────────────

/**
 * Drop-in replacement for [composable] with premium iOS-style blur + slide transitions.
 *

 *
 * @param route              Navigation route string.
 * @param arguments          Route arguments (same as composable).
 * @param deepLinks          Deep link definitions (same as composable).
 * @param durationMillis     Total animation duration. Default 550ms.
 * @param blurRadius         Peak blur radius applied to outgoing screen. Default 16.dp.
 * @param enableScale        Whether to slightly scale down the outgoing screen. Default true.
 * @param content            Screen composable content.
 */
fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    durationMillis: Int = 700,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            enterTransition(durationMillis)
        },
        exitTransition = {
            exitTransition(durationMillis)
        },
        popEnterTransition = {
            popEnterTransition(durationMillis)
        },
        popExitTransition = {
            popExitTransition(durationMillis)
        },
        content = content
    )
}

// ─── Usage Example ───────────────────────────────────────────────────────────
// fun NavGraphBuilder.detailScreen(navController: NavHostController) {
//     animatedComposable(
//         route = Destinations.DetailScreen.ruta,
//         durationMillis = 600,
//         blurRadius = 20.dp,
//         enableScale = true
//     ) {
//         DetailScreen(navController)
//     }
// }