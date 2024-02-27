package com.rainbow.drum

import android.animation.ObjectAnimator
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel: ViewModel() {

    var isWorking = false
    var finished = MutableLiveData<Boolean>()
    var randomValues = 0L
    var mainActivityUIState: MainActivityUIState? = null

    private fun prepareTimer(animator: ObjectAnimator) {
        object : CountDownTimer(randomValues, randomValues) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                animator.cancel()
                isWorking = false
                finished.postValue(true)
                finished = MutableLiveData<Boolean>()
            }
        }.start()
    }

    fun rainbowWheelClicked(animator: ObjectAnimator) {
        randomValues = Random.nextLong(10000)
        prepareTimer(animator)
    }

    fun saveState(uiState: MainActivityUIState) {
        this.mainActivityUIState = uiState
    }

}