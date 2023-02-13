package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.modelclass.ProductDetails
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class ProductDetailsFragment : Fragment() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var pBLoading: ProgressBar

    lateinit var skeletonLoading: LottieAnimationView

    private var productImageView: ImageView? = null
    private var plusButton: ImageView? = null
    private var minusButton: ImageView? = null
    private var countTextView: TextView? = null
    private var favButton: ImageView? = null
    private var nameTextView: TextView? = null
    private var weightTextView: TextView? = null
    private var priceTextView: TextView? = null
    private var detailsTextView: TextView? = null
    private var nutritionTextView: TextView? = null
    private var cartButtonTextView: TextView? = null
    private var ratingBar: RatingBar? = null
    lateinit var cartButton: CardView

    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefFavourite: DatabaseReference
    private lateinit var databaseRefAddToCart: DatabaseReference

    var productData: ProductDetails? = null
    var clickedId: String? = null

    private var count: Int? = null

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
            addFavourite(clickedId!!, favButton!!)
        }

        cartButton.setOnClickListener {
            pBLoading.visibility = View.VISIBLE
            databaseRefAddToCart.child(clickedId!!).setValue(clickedId)
                .addOnSuccessListener {
                    databaseRefProduct.child(clickedId!!).child("Added").setValue(true)
                    databaseRefProduct.child(clickedId!!).child("ItemCount").setValue(count)
                    pBLoading.visibility = View.INVISIBLE
                    cartButtonTextView?.text = "Go to Cart"
                    Toast.makeText(
                        requireContext(),
                        "Added to Cart Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        plusButton?.setOnClickListener {
            if (count != null) {
                count = count!! + 1
                countTextView?.text = count.toString()
                if (count!! > 1) {
                    minusButton?.setImageResource(R.drawable.enabled_minus)
                }
            }
        }

        minusButton?.setOnClickListener {
            if (count != null) {
                if (count!! > 1) {
                    count = count!! - 1
                    countTextView?.text = count.toString()
                }
                if (count == 1) {
                    minusButton?.setImageResource(R.drawable.minus_button)
                }
            }
        }
    }

    private fun getData() {
        val clickedId = arguments?.getString("ClickedID")
        databaseRefProduct.child(clickedId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productData = snapshot.getValue(ProductDetails::class.java)
                if (productData != null) {
                    setData()
                    skeletonLoading.visibility = View.INVISIBLE
                    cartButton.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setData() {
        Glide.with(requireContext().applicationContext)
            .load(productData?.Url)
            .placeholder(R.drawable.app_logo)
            .into(productImageView!!)

        nameTextView?.text = productData?.Name
        weightTextView?.text = productData?.Weight
        priceTextView?.text = productData?.Price
        detailsTextView?.text = productData?.Details
        ratingBar?.rating = productData?.Rating!!.toFloat()
        nutritionTextView?.text = productData?.Nutrition

        if (productData!!.FavAdded == true) {
            favButton?.setImageResource(R.drawable.favorite_filled)
        }

        countTextView?.text = productData?.ItemCount.toString()

        if (productData!!.Added == true) {
            cartButtonTextView?.text = "Go to Cart"
        }

        count = productData!!.ItemCount

        if (count!! > 1) {
            minusButton?.setImageResource(R.drawable.enabled_minus)
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
        skeletonLoading = view.findViewById(R.id.skeletonLoading)
        cartButtonTextView = view.findViewById(R.id.cartButtonTextView)
        plusButton = view.findViewById(R.id.plusButton)
        minusButton = view.findViewById(R.id.minusButton)
        countTextView = view.findViewById(R.id.countTextView)

        skeletonLoading.visibility = View.VISIBLE

        clickedId = arguments?.getString("ClickedID")

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefFavourite = FirebaseDatabase.getInstance().reference.child("Favourite")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")
    }

    private fun addFavourite(id: String, addButtonImage: ImageView) {

        databaseRefProduct.child(id).child("FavAdded")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favAddValue = snapshot.value

                    if (favAddValue == false) {
                        databaseRefFavourite.child(id).setValue(id)
                            .addOnSuccessListener {
                                addButtonImage.setImageResource(R.drawable.favorite_filled)
                                databaseRefProduct.child(id).child("FavAdded").setValue(true)
                                pBLoading.visibility = View.INVISIBLE
                                Toast.makeText(
                                    context,
                                    "Added to Favourite Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else if (favAddValue == true) {
                        databaseRefFavourite.child(id).removeValue().addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.favorite_border)
                            databaseRefProduct.child(id).child("FavAdded").setValue(false)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(
                                context,
                                "Removed From Favourite Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        bottomNavigationView.visibility = View.VISIBLE
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.visibility = View.VISIBLE
    }

}