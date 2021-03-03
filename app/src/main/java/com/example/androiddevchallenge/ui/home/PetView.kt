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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.ui.LoadState
import com.example.androiddevchallenge.ui.Screen
import com.example.androiddevchallenge.ui.components.ChipsList
import com.example.androiddevchallenge.ui.getImage
import com.example.androiddevchallenge.ui.theme.shapes

@Composable
fun PetView(
    pet: Pet,
    isEvenView: Boolean,
    navigateTo: (Screen) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(shape = shapes(isEvenView.not()).medium)
    ) {
        when (val image = getImage(pet.image.url)) {
            is LoadState.Loaded -> LoadedPetView(image, pet, isEvenView, navigateTo)
            is LoadState.Loading ->
                Box(
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
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LoadedPetView(
    image: LoadState.Loaded,
    pet: Pet,
    isEvenView: Boolean,
    navigateTo: (Screen) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    Box {
        Image(
            modifier = Modifier
                .clickable {
                    visible = visible.not()
                }
                .imageSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart,
            contentDescription = null,
            bitmap = image.bitmap.asImageBitmap()
        )
        Text(
            text = pet.name,
            textAlign = if (isEvenView) TextAlign.Start else TextAlign.End,
            modifier = (
                if (isEvenView)
                    Modifier
                        .align(Alignment.BottomStart)
                        .horizontalGradientBackground(
                            Color.White,
                            Color.Transparent
                        )
                else
                    Modifier
                        .align(Alignment.BottomEnd)
                        .horizontalGradientBackground(
                            Color.Transparent,
                            Color.White
                        )
                )
                .fillMaxWidth()
                .padding(8.dp),
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
        ) {
            ChipsList(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 32.dp),
                pet.temperament?.split(',') ?: emptyList()
            )
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                icon = { Icon(Icons.Filled.Favorite, "", tint = Color.White) },
                text = { Text(text = "More Info", color = Color.White) },
                onClick = { navigateTo(Screen.PetDetails(pet.id)) },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            )
        }
    }
}

fun Modifier.imageSize() =
    this
        .height(200.dp)
        .fillMaxWidth()

fun Modifier.horizontalGradientBackground(
    startColor: Color,
    endColor: Color,
) = gradientBackground(startColor, endColor) { gradientColors, size ->
    horizontalGradient(
        colors = gradientColors,
        startX = 0f,
        endX = size.width
    )
}

fun Modifier.gradientBackground(
    startColor: Color,
    endColor: Color,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val gradient =
        remember(startColor, endColor, size) { brushProvider(listOf(startColor, endColor), size) }
    drawWithContent {
        size = this.size
        drawRect(brush = gradient)
        drawContent()
    }
}
