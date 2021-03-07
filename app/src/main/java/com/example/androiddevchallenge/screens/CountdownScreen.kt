package com.example.androiddevchallenge.screens

import androidx.compose.animation.animateColor
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
import com.example.androiddevchallenge.CountdownViewModel

/**
 * Countdown timer composable
 * Input takes [startMinutes] to represent the timer's start time (in minutes) plus the [startSeconds]
 * to represent the timer's additional second time. (1 minute and 30 seconds, for example)
 */

enum class CountdownState {
    Started, Quarter, Half, ThreeQuarters, Done;
}

class CountdownTransitionData(
    progressColor: State<Color>,
    backgroundColor: State<Color>,
    textColor: State<Color>
) {
    val progressColor by progressColor
    val backgroundColor by backgroundColor
    val textColor by textColor
}

@Composable
fun updateTransitionData(state: CountdownState, progress: Float): CountdownTransitionData {
    val transition = updateTransition(state)

    val backgroundColor = transition.animateColor {
        when(state){
            CountdownState.Started -> Color.White
            CountdownState.Quarter -> MaterialTheme.colors.secondary
            CountdownState.Half -> MaterialTheme.colors.surface
            CountdownState.ThreeQuarters -> MaterialTheme.colors.primary
            CountdownState.Done -> MaterialTheme.colors.primary
        }
    }

    val textColor = transition.animateColor {
        when(state){
            CountdownState.Started -> Color.Black
            CountdownState.Quarter -> MaterialTheme.colors.onSecondary
            CountdownState.Half -> Color.Black
            CountdownState.ThreeQuarters -> MaterialTheme.colors.onPrimary
            CountdownState.Done -> MaterialTheme.colors.onPrimary
        }
    }

    val progressColor = transition.animateColor {
        when(state){
            CountdownState.Started -> Color.Black
            CountdownState.Quarter -> MaterialTheme.colors.onSecondary
            CountdownState.Half -> Color.Black
            CountdownState.ThreeQuarters -> MaterialTheme.colors.onPrimary
            CountdownState.Done -> MaterialTheme.colors.onPrimary
        }
    }

    return remember(transition) {
        CountdownTransitionData(
            progressColor = progressColor,
            backgroundColor = backgroundColor,
            textColor = textColor
        )
    }
}

@Composable
fun CountdownScreen(
    viewModel: CountdownViewModel
) = Box(modifier = Modifier.fillMaxSize()) {

    val updatedState =
        updateTransitionData(
            state = viewModel.countdownState,
            progress = viewModel.currentProgress
        )

    val progressTransition: Float by animateFloatAsState(
        targetValue = viewModel.currentProgress,
        animationSpec = tween(easing = LinearEasing)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = updatedState.backgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(350.dp),
            progress = progressTransition,
            strokeWidth = 16.dp,
            color = updatedState.progressColor
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.timeLeft,
            color = updatedState.textColor,
            style = MaterialTheme.typography.h3
        )
    }
}