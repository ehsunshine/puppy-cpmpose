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
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.androiddevchallenge.domain.repository.PetRepository
import com.example.androiddevchallenge.ui.NavigationViewModel
import com.example.androiddevchallenge.ui.Screen
import com.example.androiddevchallenge.ui.detail.PetDetailScreen
import com.example.androiddevchallenge.ui.home.HomeScreen
import com.example.androiddevchallenge.ui.theme.MyTheme
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val navigationViewModel by viewModels<NavigationViewModel>()
    private val petRepository by inject<PetRepository>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                AppContent(
                    navigationViewModel = navigationViewModel,
                    petRepository = petRepository
                )
            }
        }
    }

    override fun onBackPressed() {
        if (!navigationViewModel.onBack()) {
            super.onBackPressed()
        }
    }
}

@Composable
private fun AppContent(
    navigationViewModel: NavigationViewModel,
    petRepository: PetRepository,
) {
    Crossfade(navigationViewModel.currentScreen) {
        Surface(color = MaterialTheme.colors.background) {
            when (val screen = navigationViewModel.currentScreen) {
                is Screen.Home -> HomeScreen(
                    navigateTo = {
                        navigationViewModel.navigateTo(it)
                    },
                    petsRepository = petRepository
                )

                is Screen.PetDetails -> PetDetailScreen(
                    petId = screen.petId,
                    onBack = { navigationViewModel.onBack() },
                    petsRepository = petRepository
                )
            }
        }
    }
}
