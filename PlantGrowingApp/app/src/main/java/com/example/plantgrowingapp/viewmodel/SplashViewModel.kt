package com.example.plantgrowingapp.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantgrowingapp.R

class SplashViewModel : ViewModel() {

    val animationResource = R.anim.bounce

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    init {
        val timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                _navigateToLogin.value = true
            }
        }
        timer.start()
    }

    fun doneNavigating() {
        _navigateToLogin.value = null
    }
}