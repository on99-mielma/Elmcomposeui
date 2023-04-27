package com.on99.elmcomposeui.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.on99.elmcomposeui.R


private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private class FirstBigLazyLineData {
    val firstBigLazyLineData = listOf(
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
    val secondDoubleLineData = listOf(
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

}