package com.example.androiddevchallenge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController

@Composable
fun StartCountdownScreen(navController: NavHostController = rememberNavController()) {

    val buttonEnabled = remember { mutableStateOf(false) }
    val startMinutes = remember { mutableStateOf(TextFieldValue(text = "00")) }
    val startSeconds = remember { mutableStateOf(TextFieldValue(text = "10")) }

    val onClick = {
        val currentStartMinutes = startMinutes.value.text.toLong()
        val currentStartSeconds = startSeconds.value.text.toLong()

        val route = "countdown/$currentStartMinutes/$currentStartSeconds"
        navController.navigate(route = route){
            launchSingleTop = true
        }
    }

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        TextField(
            value = startMinutes.value,
            onValueChange = {
                startMinutes.value = it
                buttonEnabled.value = startMinutes.value.text.toLongOrNull() != null
                        && startSeconds.value.text.toLongOrNull() != null
            },
            label = { Text("Minutes") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        TextField(
            value = startSeconds.value,
            onValueChange = {
                startSeconds.value = it

                buttonEnabled.value = startMinutes.value.text.toLongOrNull() != null
                        && startSeconds.value.text.toLongOrNull() != null
            },
            label = { Text("Seconds") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = buttonEnabled.value
        ) {
            Text(text = "START COUNTDOWN")
        }
    }
}