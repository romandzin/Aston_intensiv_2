package com.rainbow.drum

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class RainbowZone @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    fun drawText() {
        this.requestLayout()
        this.invalidate()
    }

    val rainbowWheel: RainbowWheel by lazy {
        findViewById(R.id.wheel)
    }

    private val imageView: ImageView by lazy {
        findViewById(R.id.image)
    }

    init {
        inflate(context, R.layout.rainbow_zone, this)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if ((context as? MainActivity)?.previousTextColor != 0) {
            val paint = Paint()
            paint.color = resources.getColor(
                (context as? MainActivity)?.previousTextColor!!,
                resources.newTheme()
            )
            paint.textSize = 50F
            canvas.drawText("Hello World", width / 2.toFloat() - 100, height.toFloat() - 100, paint)
        }
    }

    fun setImage() {
        Picasso.with(context)
            .load("https://placekitten.com/640/360")
            .placeholder(R.drawable.wait)
            .error(R.drawable.error)
            .into(imageView)
        (context as? MainActivity)?.textColor = null
    }

    fun reset() {
        this.requestLayout()
        this.invalidate()
        resetImage()
    }

    private fun resetImage() {
        imageView.setImageDrawable(null)
    }
}