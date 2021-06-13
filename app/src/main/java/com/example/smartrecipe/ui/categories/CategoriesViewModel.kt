package com.example.smartrecipe.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException

class CategoriesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Categories Fragment"
    }
    val text: LiveData<String> = _text
}