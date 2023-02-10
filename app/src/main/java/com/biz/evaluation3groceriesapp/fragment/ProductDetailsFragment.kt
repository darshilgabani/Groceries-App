package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.modelclass.ProductDetails
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class ProductDetailsFragment : Fragment() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var pBLoading : ProgressBar

    private var productImageView: ImageView? = null
    private var favButton: ImageView? = null
    private var nameTextView: TextView? = null
    private var weightTextView: TextView? = null
    private var priceTextView: TextView? = null
    private var detailsTextView: TextView? = null
    private var nutritionTextView: TextView? = null
    private var ratingBar: RatingBar? = null
    lateinit var cartButton: CardView

    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefFavourite: DatabaseReference

    var productData: ProductDetails? = null
    var added : Boolean? = null

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
        val view = inflater.inflate(R.layout.fragment_productdetails, container, false)

        initVar(view)

        getData()

        onClick()

        return view
    }

    private fun onClick() {
        favButton?.setOnClickListener {
            pBLoading.visibility = View.VISIBLE
            val clickedId = arguments?.getString("ClickedID")
            addFavourite(clickedId!!,favButton!!)
        }
    }

    private fun getData() {
        val clickedId = arguments?.getString("ClickedID")
        databaseRefProduct.child(clickedId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productData = snapshot.getValue(ProductDetails::class.java)
                if (productData != null) {
                    setData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setData() {
        Glide.with(requireActivity().applicationContext)
            .load(productData?.Url)
            .placeholder(R.drawable.app_logo)
            .into(productImageView!!)

        nameTextView?.text = productData?.Name
        weightTextView?.text = productData?.Weight
        priceTextView?.text = productData?.Price
        detailsTextView?.text = productData?.Details
        ratingBar?.rating = productData?.Rating!!.toFloat()
        nutritionTextView?.text = productData?.Nutrition

        if (productData!!.FavAdded == true){
            favButton?.setImageResource(R.drawable.favorite_filled)
        }
    }

    private fun initVar(view: View) {
        productImageView = view.findViewById(R.id.productImageView)
        favButton = view.findViewById(R.id.favButton)
        nameTextView = view.findViewById(R.id.nameTextView)
        weightTextView = view.findViewById(R.id.weightTextView)
        priceTextView = view.findViewById(R.id.priceTextView)
        detailsTextView = view.findViewById(R.id.detailsTextView)
        nutritionTextView = view.findViewById(R.id.nutritionTextView)
        ratingBar = view.findViewById(R.id.ratingBar)
        cartButton = view.findViewById(R.id.cartButton)
        pBLoading = view.findViewById(R.id.pBLoading)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefFavourite = FirebaseDatabase.getInstance().reference.child("Favourite")
    }

    private fun addFavourite(id: String, addButtonImage: ImageView) {
        databaseRefFavourite.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    databaseRefFavourite.child(id).setValue(id)
                        .addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.favorite_filled)
                            databaseRefProduct.child(id).child("FavAdded").setValue(true)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Added to Favourite Successfully", Toast.LENGTH_SHORT).show()
                        }
                } else
                {
                    for(data in snapshot.children){
                        if (data.key == id){
                            added = true
                        }else if (data.key != id){
                            added = false
                        }
                    }

                    if (added == true){
                        databaseRefFavourite.child(id).removeValue().addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.favorite_border)
                            databaseRefProduct.child(id).child("FavAdded").setValue(false)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Remove From Favourite Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }else if (added == false){
                        databaseRefFavourite.child(id).setValue(id)
                            .addOnSuccessListener {
                                addButtonImage.setImageResource(R.drawable.favorite_filled)
                                databaseRefProduct.child(id).child("FavAdded").setValue(true)
                                pBLoading.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), "Added to Favourite Successfully", Toast.LENGTH_SHORT).show()
                            }
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


}