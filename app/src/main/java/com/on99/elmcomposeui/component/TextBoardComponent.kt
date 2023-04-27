package com.on99.elmcomposeui.component

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.on99.elmcomposeui.component.nestedScrollLearning.ScrollDownRefresh
import com.on99.elmcomposeui.model.TextBoard

@Composable
fun TextBoardComponent(
    shopsViewModel: ShopsViewModel,
    modifier: Modifier = Modifier
) {
    val textBoardUiState = shopsViewModel.textBoardUiState
    when (textBoardUiState) {
        is TextBoardUiState.Loading -> TextBoardLoading(modifier = modifier.fillMaxSize())
        is TextBoardUiState.Success -> TextBoardSuccessComponent(
            texts = textBoardUiState.textBoards,
            shopsViewModel = shopsViewModel,
            modifier = modifier.fillMaxSize()
        )
        is TextBoardUiState.Error -> TextBoardErrorScreen(retryAction = shopsViewModel::getTextBoardVM)
    }
}


@Composable
fun TextBoardSuccessComponent(
    texts: List<TextBoard>,
    modifier: Modifier = Modifier,
    shopsViewModel: ShopsViewModel
) {
    ScrollDownRefresh(onRefresh = shopsViewModel::getTextBoardVM,
        loadingIndicator = {
            Box(modifier = Modifier.padding(75.dp)) {
                CircularProgressIndicator(Modifier.size(50.dp))
            }
        }
    ) {
        LazyColumn(
            content = {
                items(items = texts) { item ->
                    TextBoardCard(text = item, modifier = modifier.padding(horizontal = 10.dp))
                }
            },
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBoardCard(
    text: TextBoard,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    ElevatedCard(
        onClick = {
            Log.e("Card::onClick", text.id)
            try {
                clipboardManager.setText(AnnotatedString(text = text.text))
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ClipBoard", e.toString())
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp,
            focusedElevation = 20.dp,
            hoveredElevation = 18.dp,
            disabledElevation = 0.dp,
            draggedElevation = 4.dp
        )
    ) {
        Text(
            text = text.text,
            modifier = modifier
                .padding(start = 4.dp, top = 5.dp, end = 4.dp)
                .height(77.dp),
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            fontSize = 20.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 24.sp
        )
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp)
        )
        Row(
            modifier = modifier
                .padding(top = 3.dp, bottom = 3.dp)
                .height(15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text.author,
                fontSize = 13.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 6.dp, end = 2.dp)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(15.dp)
                    .background(Color.DarkGray)
            )
            Text(
                text = text.date,
                fontSize = 13.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun TextBoardLoading(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    LazyColumn(
        content = {
            items(10) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
                ) {
                    Box(
                        modifier = modifier
                            .padding(start = 4.dp, top = 5.dp, end = 4.dp)
                            .height(77.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray.copy(alpha = alpha))
                    )
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(2.dp)
                    )
                    Row(
                        modifier = modifier
                            .padding(top = 3.dp, bottom = 3.dp)
                            .height(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = modifier
                                .height(13.dp)
                                .padding(start = 6.dp, end = 2.dp)
                                .background(Color.LightGray.copy(alpha = alpha))
                        )
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(15.dp)
                                .background(Color.DarkGray)
                        )
                        Box(
                            modifier = modifier
                                .height(13.dp)
                                .padding(start = 2.dp, end = 2.dp)
                                .background(Color.LightGray.copy(alpha = alpha))
                        )
                    }
                }
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
        contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
    )
}

@Composable
fun TextBoardErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("loading failed!!")
        Button(onClick = retryAction) {
            Text(text = "Retry")
        }
    }
}