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
package com.example.androiddevchallenge.domain.repository

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.androiddevchallenge.domain.source.network.BreedApi
import com.example.androiddevchallenge.model.Pet
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class PetRepositoryImpl(
    private val breedApi: BreedApi
) : PetRepository {
    private val pets = mutableListOf<Pet>()
    override suspend fun getPet(id: String): Result<Pet> {
        return Result.success(pets.first { it.id == id })
    }

    override suspend fun getPets(): Result<List<Pet>> {
        return try {
            withContext(coroutineContext) {
                breedApi.getAllBreeds()
                    .map {
                        it.copy(
                            description = LoremIpsum(
                                Random.nextInt(
                                    from = 30,
                                    until = 80
                                )
                            ).values.joinToString(" ")
                        )
                    }
                    .shuffled()
                    .let {
                        pets.addAll(it)
                        Result.success(it)
                    }
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
