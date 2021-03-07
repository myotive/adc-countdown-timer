package com.example.androiddevchallenge.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.R

fun Int.twoDigitString() = "%02d".format(this)

@Composable
fun StartCountdownScreen(navController: NavHostController = rememberNavController()) {

    val buttonEnabled = remember { mutableStateOf(false) }
    val startMinutes = remember { mutableStateOf("00") }
    val startSeconds = remember { mutableStateOf("00") }

    val onClick = {
        val currentStartMinutes = startMinutes.value.toLong()
        val currentStartSeconds = startSeconds.value.toLong()

        val route = "countdown/$currentStartMinutes/$currentStartSeconds"
        navController.navigate(route = route) {
            launchSingleTop = true
        }
    }

    val verifyResult = {
        buttonEnabled.value = startMinutes.value.toIntOrNull() ?: 0 > 0
                || startSeconds.value.toIntOrNull() ?: 0 > 0
    }

    return Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val onIncrementClick = {
                    if(canIncrement(startMinutes)) {
                        startMinutes.value = startMinutes.value.toInt().plus(1).twoDigitString()
                    }
                    verifyResult()
                }
                val onDecrementClick = {
                    if(canDecrement(startMinutes)) {
                        startMinutes.value = startMinutes.value.toInt().minus(1).twoDigitString()
                    }
                    verifyResult()
                }
                InputButton(
                    input = startMinutes,
                    onIncrementClick = onIncrementClick,
                    onDecrementClick = onDecrementClick
                )
                Text(text = "Minutes", modifier = Modifier.padding(5.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val onIncrementClick = {
                    if(canIncrement(startSeconds)) {
                        startSeconds.value = startSeconds.value.toInt().plus(1).twoDigitString()
                    }
                    verifyResult()
                }
                val onDecrementClick = {
                    if(canDecrement(startSeconds)) {
                        startSeconds.value = startSeconds.value.toInt().minus(1).twoDigitString()
                    }
                    verifyResult()
                }
                InputButton(
                    input = startSeconds,
                    onIncrementClick = onIncrementClick,
                    onDecrementClick = onDecrementClick
                )
                Text(text = "Seconds", modifier = Modifier.padding(5.dp))
            }
        }

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            enabled = buttonEnabled.value
        ) {
            Text(text = "START COUNTDOWN")
        }
    }
}

fun canIncrement(item: MutableState<String>): Boolean = item.value.toIntOrNull() ?: 0 < 100
fun canDecrement(item: MutableState<String>): Boolean = item.value.toIntOrNull() ?: 0 > 0

@Composable
fun InputButton(
    input: State<String>,
    onIncrementClick: () -> Unit = {},
    onDecrementClick: () -> Unit = {}
) = Column(
    modifier = Modifier
        .width(50.dp)
        .height(150.dp)
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onIncrementClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_up),
                contentDescription = "Increment"
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = input.value)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onDecrementClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "Decrement"
            )
        }
    }
}

@Preview
@Composable
fun StartCountdownScreenPreview() = StartCountdownScreen()

@Preview
@Composable
fun InputButtonPreview() =
    InputButton(
        input = remember { mutableStateOf("00") },
    )