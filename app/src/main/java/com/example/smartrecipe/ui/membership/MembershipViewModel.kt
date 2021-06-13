package com.example.smartrecipe.ui.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MembershipViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Membership Fragment"
    }
    val text: LiveData<String> = _text
}