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
package com.example.androiddevchallenge.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun getImage(url: String): LoadState {
    var state: LoadState by remember(url) {
        mutableStateOf(LoadState.Loading)
    }
    if (url.isEmpty()) {
        state = LoadState.NotLoaded
    } else {
        Picasso.get().load(url).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                state = when (bitmap) {
                    null -> {
                        LoadState.Error
                    }
                    else -> {
                        LoadState.Loaded(bitmap)
                    }
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                state = LoadState.Error
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                state = LoadState.Loading
            }
        })
    }
    return state
}

sealed class LoadState {
    object Loading : LoadState()
    object NotLoaded : LoadState()
    object Error : LoadState()
    data class Loaded(val bitmap: Bitmap) : LoadState()
}
