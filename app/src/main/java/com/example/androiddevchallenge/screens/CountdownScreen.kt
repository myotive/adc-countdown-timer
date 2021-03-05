package com.example.androiddevchallenge.screens

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Countdown timer composable
 * Input takes [startMinutes] to represent the timer's start time (in minutes) plus the [startSeconds]
 * to represent the timer's additional second time. (1 minute and 30 seconds, for example)
 */

@Composable
fun CountdownScreen(
    viewModel: CountdownViewModel
) = Box(modifier = Modifier.fillMaxSize()) {

    val progressColorTransition = updateTransition(targetState = viewModel.progressColor)
    val progressColor = progressColorTransition.animateColor {
        if (viewModel.currentProgress > 0.4f)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.secondary
    }

    val progress: Float by animateFloatAsState(
        targetValue = viewModel.currentProgress,
        animationSpec = tween(easing = LinearEasing)
    )

    val backgroundColorTransition = updateTransition(targetState = viewModel.backgroundColor)
    val backgroundColor = backgroundColorTransition.animateColor {
        if (viewModel.currentProgress > 0.4f)
            MaterialTheme.colors.background
        else
            MaterialTheme.colors.primary
    }

    val textColorTransition = updateTransition(targetState = viewModel.textColor)
    val textColor = textColorTransition.animateColor {
        if (viewModel.currentProgress > 0.4f)
            MaterialTheme.colors.onSecondary
        else
            MaterialTheme.colors.onPrimary
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor.value)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(350.dp),
            progress = progress,
            strokeWidth = 16.dp,
            color = progressColor.value
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.timeLeft,
            color = textColor.value,
            style = MaterialTheme.typography.h3
        )
    }
}

class CountdownViewModel(
    private val navController: NavHostController,
    var startMinutes: Int = 0,
    var startSeconds: Int = 0,
    progressColor: Color,
    backgroundColor: Color,
    textColor: Color
) : ViewModel() {

    /**
     * UI State
     */
    var timeLeft: String by mutableStateOf("")
        private set

    var currentProgress: Float by mutableStateOf(1.0f)
        private set

    var progressColor: Color by mutableStateOf(progressColor)

    var backgroundColor: Color by mutableStateOf(backgroundColor)

    var textColor: Color by mutableStateOf(textColor)

    /**
     * Initialization data
     */
    private val totalCountDownTimeMills
        get() = TimeUnit.MINUTES.toMillis(startMinutes.toLong()) + TimeUnit.SECONDS.toMillis((startSeconds.plus(1).toLong())
        )

    private val onTick = { millisUntilFinished: Long ->
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

        currentProgress = millisUntilFinished.toFloat() / totalCountDownTimeMills.toFloat()
        Timber.i("Current Progress = $currentProgress")

        timeLeft = "${"%02d".format(minutes)} : ${"%02d".format(seconds)} "
    }

    private val countDownTimer by lazy {
        getCountDownTimer(totalCountDownTimeMills, onTick) {
            currentProgress = 0.0f

            Handler(Looper.getMainLooper()).postDelayed({
                navController.popBackStack()
            }, 2000)
        }
    }

    /**
     * Start Countdown function
     */
    fun startCountdown(): CountDownTimer = countDownTimer.start()

    fun stopCountdown() = countDownTimer.cancel()


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
        private val navController: NavHostController,
        private val progressColor: Color,
        private val backgroundColor: Color,
        private val textColor: Color
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CountdownViewModel::class.java)) {
                return CountdownViewModel(
                    navController = navController,
                    progressColor = progressColor,
                    backgroundColor = backgroundColor,
                    textColor = textColor
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}