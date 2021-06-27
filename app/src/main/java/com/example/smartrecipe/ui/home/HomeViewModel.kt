package com.example.smartrecipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _navigateToCategories = MutableLiveData<Boolean>()
    val navigateToCategories: LiveData<Boolean>
        get() = _navigateToCategories

    fun onFabClicked() {
        _navigateToCategories.value = true
    }

    fun onNavigatedToCategories() {
        _navigateToCategories.value = false
    }
}