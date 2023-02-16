package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.biz.evaluation3groceriesapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrderAcceptFragment : Fragment() {
    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var backHomeTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orderaccept, container, false)

        iniVar(view)

        onClick()

        return view
    }

    private fun onClick() {
        backHomeTextView.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_orderAcceptFragment_to_shopFragment2)
        }
    }

    private fun iniVar(view: View) {
        backHomeTextView = view.findViewById(R.id.backHomeTextView)
    }

}