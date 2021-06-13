package com.example.smartrecipe.ui.howItWorks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HowItWorksViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is how it works Fragment"
    }
    val text: LiveData<String> = _text
}