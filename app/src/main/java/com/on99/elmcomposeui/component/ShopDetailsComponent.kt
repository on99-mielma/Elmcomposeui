package com.on99.elmcomposeui.component

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.on99.elmcomposeui.model.ShopDetails
import com.on99.elmcomposeui.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.testTag
import com.on99.elmcomposeui.component.nestedScrollLearning.ScrollDownRefresh
import com.on99.elmcomposeui.component.componentdetail.ComponentDetailUiState
import com.on99.elmcomposeui.ui.theme.PurpleGrey80


@Composable
fun ShopDetailsComponent(
    shopsViewModel: ShopsViewModel,
    //shopUiState: ShopUiState,
    //retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shopUiState = shopsViewModel.shopUiState
    val retryAction = shopsViewModel::getShopDetailVM
    when (shopUiState) {
        is ShopUiState.Loading -> LoadingComponent(
            modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 0.dp,
                bottom = 0.dp
            )
        )
        is ShopUiState.Success -> SuccessComponent(
            detail = shopUiState.shopdetails,
            modifier = modifier.padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp),
            onItemClick = shopsViewModel::updateShopCheck,
            retryAction = retryAction
        )
        is ShopUiState.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
                .background(
                    PurpleGrey80.copy(alpha = 0.5f)
                )
        )
    }
}


@Composable
fun SuccessComponent(
    detail: List<ShopDetails>,
    modifier: Modifier = Modifier,
    onItemClick: (ShopDetails) -> Unit,
    retryAction: () -> Unit
) {
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPostScroll(
//                consumed: Offset,
//                available: Offset,
//                source: NestedScrollSource
//            ): Offset {
//                if (source == NestedScrollSource.Drag && available.y > 0) {
//                    retryAction()
//                    return Offset(x = 0f, y = available.y)
//                } else {
//                    return Offset.Zero
//                }
//            }
//        }
//    }
    ScrollDownRefresh(
        onRefresh = retryAction,
        loadingIndicator = {
            Box(modifier = Modifier.padding(50.dp)) {
                CircularProgressIndicator(Modifier.size(50.dp))
            }
        }
    ) {
        LazyColumn(
            content = {
                items(items = detail) { item ->
                    ShopDetailsCard2(detail = item, onItemClick = onItemClick)
                }
            },
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 0.dp)
        )
    }
}

@Composable
fun LoadingComponent(modifier: Modifier = Modifier) {
    Box(
        //contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
//        Image(
//            modifier = Modifier.size(200.dp),
//            painter = painterResource(R.drawable.loading_img),
//            contentDescription = "LOADING......"
//        )
        LazyColumn(
            content = {
                items(10) {
                    LoadingCard()
                }
            },
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 0.dp)
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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

@Deprecated("Have New One and even better which name is <ShopDetailsCard2>")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailsCard(detail: ShopDetails, modifier: Modifier = Modifier) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.Blue.copy(0.5f), Color.Blue.copy(0.5f)),
        startX = 0.0f,
        endX = 100.0f,
        tileMode = TileMode.Repeated
    )
    Box(
        modifier = Modifier.border(
            width = 1.dp,
            brush = gradientBrush,
            shape = CircleShape.copy(topEnd = CornerSize(0), bottomEnd = CornerSize(0))
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 82.dp, max = 84.dp)
                .aspectRatio(1f),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(modifier = modifier
                .size(80.dp)
                .aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp,
                    focusedElevation = 16.dp,
                    hoveredElevation = 12.dp,
                    disabledElevation = 0.dp,
                    draggedElevation = 2.dp
                ),
                onClick = {
                    Log.e("Card::onClick", detail.imgsrc)
                }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(detail.imgsrc)
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    contentDescription = "ShopDetail",
                    contentScale = ContentScale.Crop,
                    modifier = modifier.clip(CircleShape)
                )
            }
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start
            ) {
                //Spacer(modifier = modifier.height(5.dp))
                Text(
                    modifier = modifier.padding(start = 4.dp, top = 5.dp, bottom = 5.dp),
                    text = detail.Title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                //Spacer(modifier = modifier.height(15.dp))
                Text(
                    modifier = modifier.padding(start = 4.dp, top = 10.dp, bottom = 5.dp),
                    text = detail.Descrition,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.LightGray
                )
            }
            Text(
                text = detail.id,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraLight,
                color = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailsCard2(
    detail: ShopDetails,
    modifier: Modifier = Modifier,
    onItemClick: (ShopDetails) -> Unit
) {
    ElevatedCard(
        onClick = {
            Log.e("Card::onClick", detail.imgsrc)
            onItemClick(detail)
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 82.dp, max = 84.dp)
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            focusedElevation = 16.dp,
            hoveredElevation = 12.dp,
            disabledElevation = 0.dp,
            draggedElevation = 2.dp
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(detail.imgsrc)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = "ShopDetail",
                contentScale = ContentScale.Crop,
                modifier = modifier.size(80.dp)
            )
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start
            ) {
                //Spacer(modifier = modifier.height(5.dp))
                Text(
                    modifier = modifier.padding(start = 4.dp, top = 5.dp, bottom = 5.dp),
                    text = detail.Title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                //Spacer(modifier = modifier.height(15.dp))
                Row(modifier = modifier.padding(top = 5.dp, bottom = 7.dp)) {
                    Text(
                        modifier = modifier.padding(start = 4.dp, end = 4.dp),
                        text = detail.Descrition,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray
                    )
                    Text(
                        text = "${detail.id}H",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color.DarkGray,
                        modifier = modifier.padding(
                            start = 5.dp,
                            end = 5.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit,
    componentDetailUiState: ComponentDetailUiState
) {
    BackHandler {
        onBackPress()
    }
    LazyColumn(
        modifier = modifier
            .testTag("DetailScreen")
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 24.dp)
    ) {
        item {
            DetailScreenTopBar(
                onBackPress = onBackPress,
                componentDetailUiState = componentDetailUiState
            )
            DetailScreenItem(detail = componentDetailUiState.detail, modifier = modifier)
        }
    }
}

@Composable
private fun DetailScreenTopBar(
    onBackPress: () -> Unit,
    componentDetailUiState: ComponentDetailUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPress,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp)
        ) {
            Text(
                text = componentDetailUiState.detail.Title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailScreenItem(
    detail: ShopDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val displayToast = { text: String ->
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(detail.imgsrc)
                    .crossfade(true)
                    .build(),
                contentDescription = detail.Descrition,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillWidth,
                modifier = modifier.height(160.dp)
            )
            Divider(thickness = 5.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth())
            Text(
                text = detail.Title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.padding(top = 10.dp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = detail.Descrition,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
            IconButton(
                onClick = {
                    displayToast(detail.id)
                },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.DarkGray, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.navibar_ss),
                    contentDescription = detail.Descrition,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun LoadingCard(modifier: Modifier = Modifier) {
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 82.dp, max = 84.dp)
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(80.dp)
                    .clip(CardDefaults.shape)
                    .background(Color.Gray.copy(alpha = alpha))
            )
            Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
                Box(
                    modifier = modifier
                        .padding(start = 4.dp, top = 5.dp, bottom = 5.dp)
                        .height(25.dp)
                        .width(50.dp)
                        .background(Color.Gray.copy(alpha = alpha))
                )
                Row(modifier = modifier.padding(top = 5.dp, bottom = 7.dp)) {
                    Box(
                        modifier = modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .height(15.dp)
                            .width(30.dp)
                            .background(Color.Gray.copy(alpha = alpha))
                    )
                    Box(
                        modifier = modifier
                            .padding(start = 5.dp, end = 5.dp)
                            .height(15.dp)
                            .width(30.dp)
                            .background(Color.Gray.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}