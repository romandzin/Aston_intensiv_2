package com.rainbow.drum

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class RainbowWheel(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var startAngle = 12.0F
    private val numberOfSegments = 7
    private val sweepAngle = 360F / numberOfSegments
    private val colors = arrayOf(
        R.color.red,
        R.color.orange,
        R.color.yellow,
        R.color.green,
        R.color.sky_blue,
        R.color.blue,
        R.color.violet
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        val rectF = RectF(
            0F, 0F, width.toFloat(), width.toFloat()
        )

        for (i in 0 until numberOfSegments) {
            drawWheel(paint, i, canvas, rectF)
        }

        this.pivotX = rectF.width() / 2
        this.pivotY = rectF.height() / 2
    }

    private fun drawWheel(
        paint: Paint,
        index: Int,
        canvas: Canvas,
        rectF: RectF
    ) {
        paint.color = resources.getColor(colors[index], resources.newTheme())
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
        startAngle -= sweepAngle
    }

    fun getColor(): Int? {
        for (i in 1..7) {
            if ((this.rotation in (sweepAngle * (i - 1))..(sweepAngle) * i)) {
                when (i) {
                    1 -> {
                        return 0
                    }

                    2 -> {
                        return colors[4]
                    }

                    3 -> {
                        return 0
                    }

                    4 -> {
                        return colors[6]
                    }

                    5 -> {
                        return colors[0]
                    }

                    6 -> {
                        return 0
                    }

                    7 -> {
                        return colors[2]
                    }
                }
            }
        }
        return null
    }

}