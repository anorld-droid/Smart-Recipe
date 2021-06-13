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
import com.example.smartrecipe.ui.categories.CategoriesViewModel

class Recipe : Fragment() {

    private lateinit var viewModel: RecipeViewModel
    private var _binding: FragmentHomeBinding? = null

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

//    private fun getShareIntent(): Intent {
//        val args = GameWonFragmentArgs.fromBundle(requireArguments())
//        return ShareCompat.IntentBuilder.from(requireActivity())
//            .setText(getString(R.string.share_success_text, args.numCorrect, args.numQuestions))
//            .setType("text/plain")
//            .intent
//    }
//    private fun shareSuccess() {
//        startActivity(getShareIntent())
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item!!.itemId){
//            R.id.share -> shareSuccess()
//        }
//        return super.onOptionsItemSelected(item)
//    }t

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}