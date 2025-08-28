package com.thelazybattley.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

var startRange = 0.5
var endRange = 1.0

@Composable
fun CandleStick(
    modifier: Modifier = Modifier,
    candleStickData: CandleStickData = CandleStickData(
        open = 0.60,
        close = 0.70,
        high = 0.8,
        low = 0.55
    )
) {
    val color = if (candleStickData.open > candleStickData.close) Color.Red else Color.Green
    val rangeDiff = (endRange - startRange) / endRange

    val barHeightMultiplier =
        (candleStickData.open - candleStickData.close) / (endRange - startRange)

    val barOffsetMultiplier = if (candleStickData.open > candleStickData.close) {
        (endRange - candleStickData.open) / rangeDiff
    } else {
        (endRange - candleStickData.close) / rangeDiff
    }

    val wickStartOffsetMultiplier = (endRange - candleStickData.high) / rangeDiff
    val wickEndOffsetMultiplier = (endRange - candleStickData.low) / rangeDiff

    Canvas(
        modifier = modifier
    ) {
        val barHeight = barHeightMultiplier * size.height
        val barOffset = barOffsetMultiplier * size.height
        val wickStartOffset = wickStartOffsetMultiplier * size.height
        val wickEndOffset = wickEndOffsetMultiplier * size.height
        drawRect(
            color = color,
            size = Size(
                width = size.width,
                height = abs(barHeight).toFloat()
            ),
            topLeft = Offset(
                y = barOffset.toFloat(),
                x = 0f
            )
        )
        drawLine(
            color = color,
            start = Offset(
                x = size.width / 2f,
                y = wickStartOffset.toFloat(),
            ),
            end = Offset(
                x = size.width / 2f,
                y = wickEndOffset.toFloat()
            ),
            strokeWidth = 1.dp.toPx()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewCandleStick() {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val sizeHeight = size.height
                var stepCount = 10.0
                val step = (endRange - startRange) / stepCount
                var price = endRange
                for (i in 1..stepCount.toInt()) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(
                            x = 90f,
                            y = sizeHeight * i / 10f
                        ),
                        end = Offset(
                            x = size.width,
                            y = sizeHeight * i / 10f
                        ),
                        strokeWidth = 5f
                    )
                    price -= step
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "%.2f".format(price),
                        topLeft = Offset(
                            x = 0f,
                            y = (sizeHeight * i / 10f) - 20f
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        CandleStick(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
        )
    }
}

data class CandleStickData(val open: Double, val close: Double, val high: Double, val low: Double)
