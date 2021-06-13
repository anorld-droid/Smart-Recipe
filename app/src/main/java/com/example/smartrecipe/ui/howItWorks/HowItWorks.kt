package com.example.smartrecipe.ui.howItWorks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.smartrecipe.R
import com.example.smartrecipe.databinding.FragmentHomeBinding
import com.example.smartrecipe.ui.categories.CategoriesViewModel

class HowItWorks : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.how_it_works_fragment, container, false)

    }



}