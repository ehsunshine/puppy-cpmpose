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
package com.example.androiddevchallenge.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.domain.repository.PetRepository
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.ui.LoadState
import com.example.androiddevchallenge.ui.components.ChipsList
import com.example.androiddevchallenge.ui.getImage
import com.example.androiddevchallenge.ui.home.imageSize
import com.example.androiddevchallenge.utils.produceUiState

@Composable
fun PetDetailScreen(
    petId: String,
    petsRepository: PetRepository,
    onBack: () -> Unit,
) {
    val pet = produceUiState(petsRepository, petId) {
        getPet(petId)
    }
    val petData = pet.value.data ?: return
    PetDetailView(petData, onBack)
}

@Composable
private fun PetDetailView(
    petData: Pet,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = petData.name,
                        style = MaterialTheme.typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {},
            ) {
                Icon(Icons.Filled.Favorite, "", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            PetContent(petData, modifier)
        }
    )
}

@Composable
fun PetContent(pet: Pet, modifier: Modifier) {
    when (val image = getImage(pet.image.url)) {
        is LoadState.Loaded -> Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
                contentDescription = null,
                bitmap = image.bitmap.asImageBitmap()
            )
            ChipsList(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 32.dp),
                pet.temperament?.split(',') ?: emptyList()
            )
            Card(
                Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = pet.description,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        is LoadState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .imageSize()
        ) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(16.dp)
            )
        }
        else -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .imageSize()
        ) {
            Text(text = "A lovely dog image goes here")
        }
    }
}
