package com.example.smartrecipe.ui.subscription

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

class Subscription : Fragment() {


    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(SubscriptionViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}