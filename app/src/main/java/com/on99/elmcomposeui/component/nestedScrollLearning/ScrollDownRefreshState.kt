package com.on99.elmcomposeui.component.nestedScrollLearning

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlin.ranges.coerceAtLeast

class ScrollDownRefreshState {
    private val mutatorMutex = MutatorMutex()
    private val refreshIconOffsetAnimateble = Animatable(0.dp, Dp.VectorConverter)
    val refreshIconOffset get() = refreshIconOffsetAnimateble.value
    private val _refreshIconOffsetFlow = MutableStateFlow(0f)
    val refreshIconOffsetFlow: Flow<Float> get() = _refreshIconOffsetFlow
    val isScrollDowning by derivedStateOf { refreshIconOffset != 0.dp }
    var isRefreshing: Boolean by mutableStateOf(false)
    fun updateOffsetDelta(value: Float) {
        _refreshIconOffsetFlow.value = value
    }

    suspend fun snapToOffset(value: Dp) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            refreshIconOffsetAnimateble.snapTo(value)
        }
    }

    suspend fun animateToOffset(value: Dp) {
        mutatorMutex.mutate {
            refreshIconOffsetAnimateble.animateTo(value, tween(1000))
        }
    }
}


private class ScrollDownRefreshNestedScrollConnection(
    val state: ScrollDownRefreshState,
    val height: Dp
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (source == NestedScrollSource.Drag && available.y < 0) {
            state.updateOffsetDelta(available.y)
            return if (state.isScrollDowning) Offset(x = 0f, y = available.y) else Offset.Zero
        } else {
            return Offset.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return super.onPostFling(consumed, available)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (source == NestedScrollSource.Drag && available.y > 0) {
            state.updateOffsetDelta(available.y)
            return Offset(x = 0f, y = available.y)
        } else {
            return Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (state.refreshIconOffset > height / 2) {
            state.animateToOffset(height)
            state.isRefreshing = true
        } else {
            if (state.refreshIconOffset != 0.dp){
                state.animateToOffset(0.dp)
            }

        }
        return super.onPreFling(available)
    }
}


@Composable
private fun SubcomposeScrollDownRefresh(
    indicator: @Composable () -> Unit,
    content: @Composable (Dp) -> Unit
) {
    SubcomposeLayout { constraints: Constraints ->
        var indicatorPlaceable = subcompose("indicator", indicator).first().measure(constraints)
        var contentPlaceable = subcompose("content") {
            content(indicatorPlaceable.height.toDp())
        }.map {
            it.measure(constraints)
        }.first()
        layout(width = contentPlaceable.width, height = contentPlaceable.height) {
            contentPlaceable.placeRelative(0, 0)
        }
    }
}

@Composable
fun ScrollDownRefresh(
    onRefresh: suspend () -> Unit,
    state: ScrollDownRefreshState = remember {
        ScrollDownRefreshState()
    },
    loadingIndicator: @Composable () -> Unit = {
        CircularProgressIndicator()
    },
    content: @Composable () -> Unit
) {
    SubcomposeScrollDownRefresh(indicator = loadingIndicator) { height ->
        val SDR_NestedScrollConnection = remember(state, height) {
            ScrollDownRefreshNestedScrollConnection(state, height)
        }
        Box(
            modifier = Modifier.nestedScroll(SDR_NestedScrollConnection),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(modifier = Modifier.offset(y = -height + state.refreshIconOffset).zIndex(-2f)) {
                loadingIndicator()
            }
            Box(modifier = Modifier.offset(y = state.refreshIconOffset)) {
                content()
            }
        }
        var density = LocalDensity.current
        LaunchedEffect(Unit) {
            state.refreshIconOffsetFlow.collect {
                var currentOffset = with(density) {
                    state.refreshIconOffset + it.toDp()
                }
                state.snapToOffset(currentOffset.coerceAtLeast(0.dp).coerceAtMost(height))
            }
        }
        LaunchedEffect(key1 = state.isRefreshing) {
            if (state.isRefreshing) {
                onRefresh()
                state.animateToOffset(0.dp)
                state.isRefreshing = false
            }
        }
    }
}