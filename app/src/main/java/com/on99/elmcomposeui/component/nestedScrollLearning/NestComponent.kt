package com.on99.elmcomposeui.component.nestedScrollLearning

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Deprecated("Just for learn please check same package ScrollDownRefreshState.kt")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestComponent() {
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(
        LocalDensity.current
    ) {
        toolbarHeight.roundToPx().toFloat()
    }
    val toolbarOffsetHeightPx = remember {
        mutableStateOf(0f)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.x
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column {
            LazyRow(contentPadding = PaddingValues(top = 48.dp)) {
                items(100) {
                    Text(
                        text = "I'm item $it", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            LazyRow(
                contentPadding = PaddingValues(top = 48.dp),
                modifier = Modifier,) {
                items(50) {
                    Text(
                        text = "I'm item $it", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
        TopAppBar(modifier = Modifier
            .height(toolbarHeight)
            .offset { IntOffset(x = toolbarOffsetHeightPx.value.roundToInt(), y = 0) },
            title = {
                Text(text = "toolbar offset is ${toolbarOffsetHeightPx.value}")
            })
    }
}

