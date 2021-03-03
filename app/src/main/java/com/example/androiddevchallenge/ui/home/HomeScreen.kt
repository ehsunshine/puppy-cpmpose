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
package com.example.androiddevchallenge.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.domain.repository.PetRepository
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.ui.Screen
import com.example.androiddevchallenge.ui.state.UiState
import com.example.androiddevchallenge.utils.produceUiState

@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit,
    petsRepository: PetRepository,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState = produceUiState(petsRepository) {
        getPets()
    }

    HomeScreen(
        pets = uiState.value,
        navigateTo = navigateTo,
        scaffoldState = scaffoldState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    pets: UiState<List<Pet>>,
    navigateTo: (Screen) -> Unit,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val title = stringResource(id = R.string.app_name)
            TopAppBar(
                title = { Text(text = title) },
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            HomeContent(
                pets = pets,
                navigateTo = navigateTo,
                modifier = modifier
            )
        }
    )
}

@Composable
private fun HomeContent(
    pets: UiState<List<Pet>>,
    navigateTo: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    if (pets.data != null) {
        PetsList(pets.data, navigateTo, modifier)
    } else if (!pets.hasError) {
        Text(
            stringResource(id = R.string.loading),
            modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else {
        Text(
            stringResource(id = R.string.load_error),
            modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PetsList(
    pets: List<Pet>,
    navigateTo: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(pets) { index, pet ->
            PetView(pet, index % 2 != 0, navigateTo)
        }
    }
}
