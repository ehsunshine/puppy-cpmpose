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
package com.example.androiddevchallenge.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import com.example.androiddevchallenge.ui.state.UiState
import com.example.androiddevchallenge.ui.state.copyWithResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun <Producer, T> produceUiState(
    producer: Producer,
    block: suspend Producer.() -> Result<T>
): State<UiState<T>> {

    return produceState(UiState(loading = true), producer) {
        value = UiState(loading = true)

        value = value.copyWithResult(producer.block())
    }
}
