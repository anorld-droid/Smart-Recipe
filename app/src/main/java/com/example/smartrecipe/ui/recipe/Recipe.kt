package com.example.smartrecipe.ui.recipe

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import com.example.smartrecipe.R
import com.example.smartrecipe.databinding.FragmentHomeBinding
import com.example.smartrecipe.databinding.FragmentRecipeBinding
import com.example.smartrecipe.ui.categories.CategoriesViewModel

class Recipe : Fragment() {

    private lateinit var viewModel: RecipeViewModel
    private var _binding: FragmentRecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        viewModel =
            ViewModelProvider(this).get(RecipeViewModel::class.java)

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater .inflate(R.menu.share, menu)
//        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)){
//            menu?.findItem(R.id.share)?.setVisible(false)
////        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}