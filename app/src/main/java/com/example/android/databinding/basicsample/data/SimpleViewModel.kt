/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.databinding.basicsample.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


class SimpleViewModel : ViewModel() {
    private val _name = MutableLiveData("Ada")
    private val _lastName = MutableLiveData("Lovelace")
    private val _likes = MutableLiveData(0)

    val name: LiveData<String>
        get() = _name
    val lastName: LiveData<String>
        get() = _lastName
    val likes: LiveData<Int>
        get() = _likes

    // popularity is exposed as LiveData using a Transformation instead of a @Bindable property.
    val popularity: LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    var isCleared = false
    fun onLike() {
        if (!isCleared) {
            _name.value = ""
            _lastName.value = ""
            isCleared = true
        }
        _likes.value = (_likes.value ?: 0) + 1
        if (_likes.value!! < 9) {
            val namesList = listOf("", "S", "ü", "l", "e", "y", "m", "a", "n")
            _name.value = _name.value + namesList[_likes.value!!]
        } else if (_likes.value!! < 14) {
            val namesList = listOf("S", "e", "z", "e", "r")
            val listIndis = _likes.value!! - 9
            _lastName.value = _lastName.value + namesList[listIndis]
        } else {
            _likes.value = _likes.value!! - 1
        }

    }

    fun onDislike() {
        if (lastName.value?.isNotEmpty()!!) {
            _lastName.value = _lastName.value!!.substring(0, _lastName.value!!.length - 1)
            _likes.value = _likes.value!! - 1
        } else if (_name.value!!.isNotEmpty()) {
            _name.value = _name.value!!.substring(0, _name.value!!.length - 1)
            _likes.value = _likes.value!! - 1
        }
    }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}
