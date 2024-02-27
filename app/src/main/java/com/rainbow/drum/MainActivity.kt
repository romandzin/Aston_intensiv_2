package com.rainbow.drum

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var rainbowZone: RainbowZone
    var previousTextColor = 0
    private var isShowingImage = false
    var textColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val params = rainbowZone.rainbowWheel.layoutParams
        params.height = 50 * 12
        params.width = (50 * 10)
        rainbowZone.rainbowWheel.layoutParams = params
    }

    private fun initViews() {
        val doBigger = findViewById<SeekBar>(R.id.doBiggerSeekBar)
        rainbowZone = findViewById(R.id.rainbowZone)
        val resetButton = findViewById<AppCompatButton>(R.id.resetButton)
        doBigger.progress = 50
        doBigger.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateWheelParams(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        rainbowZone.rainbowWheel.setOnClickListener {
            val animator = createAnimation()
            rainbowZone.rainbowWheel.rotation = 0F
            mainViewModel.rainbowWheelClicked(animator)
            mainViewModel.isWorking = true
            mainViewModel.finished.observe(this) {
                endOfSpin()
            }
        }
        resetButton.setOnClickListener {
            resetRainbowZone()
        }
    }

    private fun updateWheelParams(progress: Int) {
        val params = rainbowZone.rainbowWheel.layoutParams
        params.height = progress * 12
        params.width = (progress * 10)
        if (progress == 0) {
            params.height = 1
            params.width = 1
        }
        rainbowZone.rainbowWheel.layoutParams = params
    }

    private fun resetRainbowZone() {
        textColor = null
        previousTextColor = 0
        isShowingImage = false
        rainbowZone.reset()
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.isWorking) {
            val animator = createAnimation()
            mainViewModel.finished.observe(this) {
                endOfSpin()
                animator.cancel()
            }
        }
        if (mainViewModel.mainActivityUIState != null) {
            restorePreviousState()
        }
    }

    private fun restorePreviousState() {
        previousTextColor = mainViewModel.mainActivityUIState!!.previousTextColor
        rainbowZone.drawText()
        isShowingImage = mainViewModel.mainActivityUIState!!.showingImage
        if (isShowingImage) rainbowZone.setImage()
        rainbowZone.rainbowWheel.rotation = mainViewModel.mainActivityUIState!!.rotation
    }

    private fun endOfSpin() {
        textColor = rainbowZone.rainbowWheel.getColor()
        if (textColor == 0) {
            showImage()
        } else {
            showText()
        }
        mainViewModel.finished.removeObservers(this)
    }

    private fun showText() {
        previousTextColor = textColor!!
        rainbowZone.drawText()
    }

    private fun showImage() {
        isShowingImage = true
        rainbowZone.setImage()
    }

    private fun createAnimation(): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(
            rainbowZone.rainbowWheel,
            View.ROTATION, 0F, 360f
        )
        animator.setDuration(1000)
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = -1
        animator.start()
        return animator
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveState(
            MainActivityUIState(
                previousTextColor,
                isShowingImage,
                rainbowZone.rainbowWheel.rotation
            )
        )
    }
}