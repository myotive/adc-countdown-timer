/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.viewmodels

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.androiddevchallenge.screens.CountdownState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CountdownViewModel(
    private val navController: NavHostController,
    var startMinutes: Int = 0,
    var startSeconds: Int = 0
) : ViewModel() {

    /**
     * UI State
     */
    var timeLeft: String by mutableStateOf("")
        private set

    var countdownState: CountdownState by mutableStateOf(CountdownState.Started)
        private set

    var currentProgress: Float by mutableStateOf(1.0f)
        private set

    /**
     * Initialization data
     */
    private val totalCountDownTimeMills
        get() = TimeUnit.MINUTES.toMillis(startMinutes.toLong()) + TimeUnit.SECONDS.toMillis(
            (startSeconds.plus(1).toLong())
        )

    private val onTick = { millisUntilFinished: Long ->
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

        currentProgress = millisUntilFinished.toFloat() / totalCountDownTimeMills.toFloat()
        Timber.i("Current Progress: $currentProgress")
        countdownState = when (currentProgress) {
            in 0.75f..1.00f -> CountdownState.Started
            in 0.50f..0.75f -> CountdownState.Quarter
            in 0.25f..0.50f -> CountdownState.Half
            in 0.0f..0.25f -> CountdownState.ThreeQuarters
            else -> CountdownState.Done
        }
        Timber.i("Current State: $countdownState")

        timeLeft = "${"%02d".format(minutes)} : ${"%02d".format(seconds)} "
    }

    private var countDownTimer: CountDownTimer? = null

    /**
     * Start Countdown function
     */
    fun startCountdown() {
        countDownTimer = getCountDownTimer(totalCountDownTimeMills, onTick) {
            currentProgress = 0.0f

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    navController.popBackStack()
                },
                2000
            )
        }

        countDownTimer?.start()
    }

    fun stopCountdown() = countDownTimer?.cancel()

    private fun getCountDownTimer(
        totalCountDownTimeMills: Long,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) = object : CountDownTimer(totalCountDownTimeMills, 100) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            onFinish()
        }
    }

    class Factory(
        private val navController: NavHostController
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CountdownViewModel::class.java)) {
                return CountdownViewModel(
                    navController = navController
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
