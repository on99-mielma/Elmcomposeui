package com.on99.elmcomposeui.component

import android.util.Log
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { Log.e("MapButton::Button", "Clicked...") },
        modifier = Modifier.width(16.dp)
    ) {
        Text(text = "TODO")
    }
}