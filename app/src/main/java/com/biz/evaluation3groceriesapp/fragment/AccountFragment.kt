package com.biz.evaluation3groceriesapp.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.biz.evaluation3groceriesapp.MainActivity
import com.biz.evaluation3groceriesapp.R
import java.util.*


class AccountFragment : Fragment() {

    lateinit var orderLayout : ConstraintLayout
    lateinit var logOutButton : CardView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        initVar(view)

        onClick()

        return view
    }

    private fun onClick() {
        logOutButton.setOnClickListener {
            editSharedPreferences.putInt("loginPref",2).apply()
            Navigation.findNavController(requireView()).navigate(R.id.action_accountFragment_to_loginActivity)
        }

        orderLayout.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_accountFragment_to_orderListFragment)
        }
    }

    private fun initVar(view: View) {
        orderLayout = view.findViewById(R.id.orderLayout)
        logOutButton = view.findViewById(R.id.logOutButton)

        sharedPreferences = requireActivity().getSharedPreferences("GroceriesApp", AppCompatActivity.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()
    }

}