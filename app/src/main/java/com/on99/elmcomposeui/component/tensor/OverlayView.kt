package com.on99.elmcomposeui.component.tensor


import android.graphics.RectF
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.vector.DefaultStrokeLineWidth
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.toRect
import androidx.lifecycle.whenStarted
import androidx.lifecycle.withStarted
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.LinkedList
import kotlin.math.max

val controlResults = listOf<Detection>(
    Detection.create(
        RectF(69.0f, 58.0f, 227.0f, 171.0f),
        listOf<Category>(Category.create("cat", "cat", 0.77734375f))
    ),
    Detection.create(
        RectF(13.0f, 6.0f, 283.0f, 215.0f),
        listOf<Category>(Category.create("couch", "couch", 0.5859375f))
    ),
    Detection.create(
        RectF(45.0f, 27.0f, 257.0f, 184.0f),
        listOf<Category>(Category.create("chair", "chair", 0.55078125f))
    )
)

@Composable
fun OverlayView() {
    var results: List<Detection> = LinkedList<Detection>()
    var boxPaint = Paint()
    var textBackgroundPaint = Paint()
    var textPaint = Paint()
    val BOUNDING_RECT_TEXT_PADDING = 8
    var scaleFactor: Float = 1f
    val TAG = "OverlayView"
    lateinit var bounds: Rect

    fun initPaint() {
        textBackgroundPaint.color = Color.Black
        textBackgroundPaint.style = PaintingStyle.Fill

        textPaint.color = Color.White
        textPaint.style = PaintingStyle.Fill

        boxPaint.color = Color.Green
        boxPaint.strokeWidth = 8f
        boxPaint.style = PaintingStyle.Stroke
    }

    fun init() {
        initPaint()
    }


    fun clear() {
        textPaint = Paint()
        textBackgroundPaint = Paint()
        boxPaint = Paint()
        initPaint()
    }

    fun draw(canvas: Canvas) {
        for (result in results) {
            val boundingBox = result.boundingBox
            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            val drawableRect = Rect(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)

            val drawbleText =
                result.categories[0].label + " " + "%.2f".format(result.categories[0].score)
            val textWidth = drawbleText.length
            val textHeight = 2
            canvas.drawRect(
                left,
                top,
                left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )
            Log.d(TAG, "draw: ${drawbleText}")
        }
    }

    @Composable
    fun setResults(
        detectionResults: MutableList<Detection>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        results = detectionResults
        scaleFactor = max(
            LocalView.current.width * 1f / imageWidth,
            LocalView.current.height * 1f / imageHeight
        )
    }

}

@Composable
fun OverlayNewView(
    results: List<Detection> = LinkedList<Detection>(),
    BOUNDING_RECT_TEXT_PADDING: Int = 8,
    scaleFactor: Float = 1f,
    first_rect_color: Color = Color.Yellow,
    first_rect_strokeWidth: Float = 5f,
    first_rect_alpha: Float = 0.9f,
    back_text_rect_color: Color = Color.Red,
    back_text_rect_alpha: Float = 0.5f,
    text_color: Color = Color.White
) {
    val textMeasurer = rememberTextMeasurer()
    val TAG = "OverlayNewView"
//    var count by remember { mutableStateOf(0) }
//    var count = 0
//    Log.d(TAG, "OverlayNewView: first time check count = $count")
    for (result in results) {
//        val boundingBox = result.boundingBox
//        val top = boundingBox.top * scaleFactor
//        val bottom = boundingBox.bottom * scaleFactor
//        val left = boundingBox.left * scaleFactor
//        val right = boundingBox.right * scaleFactor
//        val drawbleText =
//            result.categories[0].label + " " + "%.2f".format(result.categories[0].score)
//        val measuredText = textMeasurer.measure(
//            text = drawbleText,
//            style = TextStyle(fontSize = 18.sp)
//        )

        Spacer(modifier = Modifier.drawWithContent {
            makeADetectRect(
                result = result,
                textMeasurer = textMeasurer,
                scaleFactor,
                first_rect_color,
                first_rect_strokeWidth,
                first_rect_alpha,
                back_text_rect_color,
                back_text_rect_alpha,
                text_color
            )
        })


//        androidx.compose.foundation.Canvas(
//            modifier = Modifier.fillMaxSize(),
//            onDraw = {
//                drawLine(
//                    color = first_rect_color,
//                    start = Offset(top, left),
//                    end = Offset(top, right),
//                    strokeWidth = first_rect_strokeWidth,
//                    alpha = first_rect_alpha
//                )
//                drawLine(
//                    color = first_rect_color,
//                    start = Offset(top, right),
//                    end = Offset(bottom, right),
//                    strokeWidth = first_rect_strokeWidth,
//                    alpha = first_rect_alpha
//                )
//                drawLine(
//                    color = first_rect_color,
//                    start = Offset(top, left),
//                    end = Offset(bottom, left),
//                    strokeWidth = first_rect_strokeWidth,
//                    alpha = first_rect_alpha
//                )
//                drawLine(
//                    color = first_rect_color,
//                    start = Offset(bottom, left),
//                    end = Offset(bottom, right),
//                    strokeWidth = first_rect_strokeWidth,
//                    alpha = first_rect_alpha
//                )
//                drawRect(
//                    color = back_text_rect_color,
//                    size = measuredText.size.toSize(),
//                    topLeft = Offset(top, left),
//                    alpha = back_text_rect_alpha,
//                )
//                drawText(measuredText, color = text_color, topLeft = Offset(top, left))
//
//            }
//
//        )
    }

    // i need to clean the canvas i drawed
//    val recompose = currentRecomposeScope
//    recompose.invalidate()
//    Log.d(TAG, "OverlayNewView: recompose invalidate")
}

fun DrawScope.makeADetectRect(
    result: Detection,
    textMeasurer: TextMeasurer,
    scaleFactor: Float = 8f,
    first_rect_color: Color = Color.Yellow,
    first_rect_strokeWidth: Float = 5f,
    first_rect_alpha: Float = 0.9f,
    back_text_rect_color: Color = Color.Red,
    back_text_rect_alpha: Float = 0.5f,
    text_color: Color = Color.White
) {

    val boundingBox = result.boundingBox
    val top = if (boundingBox.top * 4f <= 0) {
        0f
    } else {
        boundingBox.top * 4f
    }
    val bottom = boundingBox.bottom * 4f
    val left = if (boundingBox.left * scaleFactor <= 0) {
        0f
    } else {
        boundingBox.left * scaleFactor
    }
    val right = boundingBox.right * scaleFactor * 2f
    val drawbleText =
        result.categories[0].label + " " + "%.2f".format(result.categories[0].score)
    val measuredText = textMeasurer.measure(
        text = drawbleText,
        style = TextStyle(fontSize = 18.sp)
    )
    val resultText = textMeasurer.measure(
        text = "<${result}>"
    )
    drawLine(
        color = first_rect_color,
        start = Offset(left, top),
        end = Offset(right, top),
        strokeWidth = first_rect_strokeWidth,
        alpha = first_rect_alpha
    )
    drawLine(
        color = first_rect_color,
        start = Offset(right, top),
        end = Offset(right, bottom),
        strokeWidth = first_rect_strokeWidth,
        alpha = first_rect_alpha
    )
    drawLine(
        color = first_rect_color,
        start = Offset(left, top),
        end = Offset(left, bottom),
        strokeWidth = first_rect_strokeWidth,
        alpha = first_rect_alpha
    )
    drawLine(
        color = first_rect_color,
        start = Offset(left, bottom),
        end = Offset(right, bottom),
        strokeWidth = first_rect_strokeWidth,
        alpha = first_rect_alpha
    )
    drawRect(
        color = back_text_rect_color,
        size = measuredText.size.toSize(),
        topLeft = Offset(left, top),
        alpha = back_text_rect_alpha,
    )
    drawText(measuredText, color = text_color, topLeft = Offset(left, top))
    drawText(resultText, color = Color.White, topLeft = Offset(left, bottom))
}

@Composable
fun EachFrameUpdatingCanvas(modifier: Modifier, onDraw: DrawScope.(Long) -> Unit) {
    var frameTime by remember {
        mutableStateOf(0L)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.whenStarted {
            while (true) {
                frameTime = withFrameMillis { it }
            }
        }
    }
    androidx.compose.foundation.Canvas(modifier = modifier) {
        onDraw(frameTime)
    }
}