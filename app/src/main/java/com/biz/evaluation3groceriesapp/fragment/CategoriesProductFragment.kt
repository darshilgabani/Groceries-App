package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.biz.evaluation3groceriesapp.R

class CategoriesProductFragment : Fragment() {
    var clickedId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categoriesproduct, container, false)

        initVar(view)

        return view
    }

    private fun initVar(view: View) {
        clickedId = arguments?.getString("ClickedID")
        Toast.makeText(requireContext(), clickedId, Toast.LENGTH_SHORT).show()
    }

}