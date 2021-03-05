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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.screens.CountdownScreen
import com.example.androiddevchallenge.screens.CountdownViewModel
import com.example.androiddevchallenge.screens.StartCountdownScreen
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                CountdownApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun CountdownApp() {
    val navController = rememberNavController()
    val viewModelFactory = CountdownViewModel.Factory(
        navController = navController,
        progressColor = MaterialTheme.colors.secondary,
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background
    )
    val viewModel: CountdownViewModel = viewModel(factory = viewModelFactory)

    navController.setLifecycleOwner(LocalLifecycleOwner.current)
    navController.setOnBackPressedDispatcher(OnBackPressedDispatcher {
        viewModel.stopCountdown()
    })

    Surface(color = MaterialTheme.colors.background) {
        NavHost(navController = navController, startDestination = "startCountdown") {
            composable(route = "startCountdown") {
                StartCountdownScreen(navController = navController)
            }

            composable(
                route = "countdown/{startMinutes}/{startSeconds}",
                arguments = listOf(
                    navArgument("startMinutes") { defaultValue = 0 },
                    navArgument("startSeconds") { defaultValue = 0 }
                ),
            ) {
                viewModel.startMinutes = it.arguments?.getInt("startMinutes") ?: 0
                viewModel.startSeconds = it.arguments?.getInt("startSeconds") ?: 0

                CountdownScreen(viewModel)

                viewModel.startCountdown()
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        CountdownApp()
    }
}

@Preview("Dark Theme", widthDp = 1080, heightDp = 1260)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        CountdownApp()
    }
}
