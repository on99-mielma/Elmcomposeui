package com.on99.elmcomposeui.component

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.on99.elmcomposeui.R


@Composable
fun FirstBigLazyLineOne(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    val temp = stringResource(id = text)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { Log.e("FirstBigLazyLineOne", temp) },
            modifier = Modifier
                .size(55.dp)
        ) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(55.dp)
            )
        }
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFromBaseline(top = 16.dp, bottom = 8.dp)
        )
    }
}

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private val firstBigLazyLineData = listOf(
    R.drawable.first_big_lazy_line_01 to R.string.first_big_line_01,
    R.drawable.first_big_lazy_line_02 to R.string.first_big_line_02,
    R.drawable.first_big_lazy_line_03 to R.string.first_big_line_03,
    R.drawable.first_big_lazy_line_04 to R.string.first_big_line_04,
    R.drawable.first_big_lazy_line_05 to R.string.first_big_line_05,
    R.drawable.first_big_lazy_line_06 to R.string.first_big_line_06,
    R.drawable.first_big_lazy_line_07 to R.string.first_big_line_07
).map {
    DrawableStringPair(it.first, it.second)
}
private val secondDoubleLineData = listOf(
    R.drawable.second_double_line_01 to R.string.second_double_line_s_01,
    R.drawable.second_double_line_02 to R.string.second_double_line_s_02,
    R.drawable.second_double_line_03 to R.string.second_double_line_s_03,
    R.drawable.second_double_line_04 to R.string.second_double_line_s_04,
    R.drawable.second_double_line_05 to R.string.second_double_line_s_05,
    R.drawable.second_double_line_06 to R.string.second_double_line_s_06,
    R.drawable.second_double_line_07 to R.string.second_double_line_s_07,
    R.drawable.second_double_line_08 to R.string.second_double_line_s_08,
    R.drawable.second_double_line_09 to R.string.second_double_line_s_09,
    R.drawable.second_double_line_10 to R.string.second_double_line_s_10,
    R.drawable.second_double_line_11 to R.string.second_double_line_s_11,
    R.drawable.second_double_line_12 to R.string.second_double_line_s_12,
    R.drawable.second_double_line_13 to R.string.second_double_line_s_13,
    R.drawable.second_double_line_14 to R.string.second_double_line_s_14,
    R.drawable.second_double_line_15 to R.string.second_double_line_s_15,
    R.drawable.second_double_line_16 to R.string.second_double_line_s_16,
    R.drawable.second_double_line_17 to R.string.second_double_line_s_17,
    R.drawable.second_double_line_18 to R.string.second_double_line_s_18,
    R.drawable.second_double_line_19 to R.string.second_double_line_s_19,
    R.drawable.second_double_line_20 to R.string.second_double_line_s_20
).map {
    DrawableStringPair(it.first, it.second)
}

@Composable
fun FirstBigLazyLine(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.heightIn(min = 20.dp, max = 25.dp))
    LazyRow(
        content = {
            items(items = firstBigLazyLineData) { item ->
                FirstBigLazyLineOne(drawable = item.drawable, text = item.text)
            }
        },
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        contentPadding = PaddingValues(horizontal = 40.dp)
    )
}


@Composable
fun SecondDoubleLazyOne(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    val temp = stringResource(id = text)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { Log.e("SecondDoubleLazyOne", temp) },
            modifier = Modifier.size(39.dp)
        ) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(39.dp)
            )
        }
        Text(
            text = stringResource(id = text),
            modifier = Modifier
                .padding(
                    top = 0.dp,
                    bottom = 0.dp
                ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SecondDoubleLazyGrid(
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .height(150.dp),
        contentPadding = PaddingValues(horizontal = 48.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(44.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(items = secondDoubleLineData) {
            SecondDoubleLazyOne(drawable = it.drawable, text = it.text)
        }
    }
}



















