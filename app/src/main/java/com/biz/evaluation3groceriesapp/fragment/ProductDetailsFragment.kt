package com.biz.evaluation3groceriesapp.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class ProductDetailsFragment : Fragment() {

    lateinit var bottomNavigationView : BottomNavigationView

    private var productImageView : ImageView? = null
    private var nameTextView : TextView? = null
    private var weightTextView : TextView? = null
    private var priceTextView : TextView? = null
    private var detailsTextView : TextView? = null
    private var nutritionTextView : TextView? = null
    private var ratingBar : RatingBar? = null
    lateinit var cartButton : CardView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    var exclData : ExclusiveOffer? = null
    var bestData : BestSelling? = null
    var clicked : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_productdetails, container, false)

        initVar(view)

        getData()

//        setData()

        return view
    }

    private fun setData() {
        Glide.with(requireContext())
            .load(exclData?.Url)
            .placeholder(R.drawable.app_logo)
            .into(productImageView!!)

        nameTextView?.text = exclData?.Name
        weightTextView?.text = exclData?.Weight
        priceTextView?.text = exclData?.Price
    }

    private fun getData() {
        clicked = sharedPreferences.getString("Clicked","")

        when(clicked){
            "ExclusiveOffer" -> {
                val jsonData = arguments?.getString("ExclModel")
                exclData = Gson().fromJson(jsonData,ExclusiveOffer::class.java)

                Glide.with(requireContext())
                    .load(exclData?.Url)
                    .placeholder(R.drawable.app_logo)
                    .into(productImageView!!)

                nameTextView?.text = exclData?.Name
                weightTextView?.text = exclData?.Weight
                priceTextView?.text = exclData?.Price
                detailsTextView?.text = exclData?.Details
                ratingBar?.rating = exclData?.Rating!!.toFloat()
                nutritionTextView?.text = exclData?.Nutrition
            }
            "BestSelling" -> {
                val jsonData = arguments?.getString("BestModel")
                bestData = Gson().fromJson(jsonData,BestSelling::class.java)

                Glide.with(requireContext())
                    .load(bestData?.Url)
                    .placeholder(R.drawable.app_logo)
                    .into(productImageView!!)

                nameTextView?.text = bestData?.Name
                weightTextView?.text = bestData?.Weight
                priceTextView?.text = bestData?.Price
                detailsTextView?.text = bestData?.Details
                ratingBar?.rating = bestData?.Rating!!.toFloat()
                nutritionTextView?.text = bestData?.Nutrition
            }
        }

    }

    private fun initVar(view: View) {
        productImageView = view.findViewById(R.id.productImageView)
        nameTextView = view.findViewById(R.id.nameTextView)
        weightTextView = view.findViewById(R.id.weightTextView)
        priceTextView = view.findViewById(R.id.priceTextView)
        detailsTextView = view.findViewById(R.id.detailsTextView)
        nutritionTextView = view.findViewById(R.id.nutritionTextView)
        ratingBar = view.findViewById(R.id.ratingBar)
        cartButton = view.findViewById(R.id.cartButton)

        sharedPreferences = requireActivity().getSharedPreferences("Login Data", AppCompatActivity.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()
    }

}