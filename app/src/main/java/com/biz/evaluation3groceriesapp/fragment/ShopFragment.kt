package com.biz.evaluation3groceriesapp.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.BestSellingAdapter
import com.biz.evaluation3groceriesapp.adapter.ExclusiveOfferAdapter
import com.biz.evaluation3groceriesapp.clicklistener.ShopClickListener
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.google.firebase.database.*

class ShopFragment : Fragment(), ShopClickListener {
    private var cartAddValue: Any? = null
    private var isAdded: Boolean? = null

    lateinit var recyclerViewExclOffer: RecyclerView
    private lateinit var recyclerViewBestSelling: RecyclerView
    private lateinit var seeAllTextViewBest: TextView
    private lateinit var seeAllTextViewExcl: TextView

    lateinit var pBExclOffer: ProgressBar
    lateinit var pBBestSelling: ProgressBar
    lateinit var pBLoading: ProgressBar

    lateinit var skeletonLoading: LottieAnimationView

    private lateinit var databaseRefExclOffer: DatabaseReference
    private lateinit var databaseRefBestSelling: DatabaseReference
    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    private var adapterBest: BestSellingAdapter? = null
    var adapterExcl: ExclusiveOfferAdapter? = null

    var listExclOffer: ArrayList<ExclusiveOffer>? = ArrayList()
    var listBestSelling: ArrayList<BestSelling>? = ArrayList()

    val indexExclList: ArrayList<String> = ArrayList()
    val indexBestList: ArrayList<String> = ArrayList()

    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        initVar(view)

        getData()

        setLayout()

        onClick()

        adapterExcl()

        adapterBest()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedPreferences.getInt("loginPref", 0) == 1) {
                    requireActivity().finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onClick() {
        seeAllTextViewExcl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ClickedID", "20017")
            bundle.putString("ClickedName", "Exclusive Offer")
            Navigation.findNavController(requireView())
                .navigate(R.id.action_shopFragment_to_categoriesProductFragment, bundle)
        }

        seeAllTextViewBest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ClickedID", "20018")
            bundle.putString("ClickedName", "Best Selling")
            Navigation.findNavController(requireView())
                .navigate(R.id.action_shopFragment_to_categoriesProductFragment, bundle)
        }
    }

    private fun setLayout() {
        gridLayoutManager = GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewExclOffer.layoutManager = gridLayoutManager

        recyclerViewBestSelling.layoutManager =
            GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun adapterExcl() {
        adapterExcl = ExclusiveOfferAdapter(listExclOffer!!)
        adapterExcl?.setSelectListener(this)
        recyclerViewExclOffer.adapter = adapterExcl
    }

    private fun adapterBest() {
        adapterBest = BestSellingAdapter(listBestSelling!!)
        adapterBest?.setSelectListener(this)
        recyclerViewBestSelling.adapter = adapterBest
    }

    private fun getExclIndex() {
        skeletonLoading.visibility = View.VISIBLE
        listExclOffer?.clear()
        databaseRefExclOffer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val index = data.value.toString()
                    indexExclList.add(index)
                }
                if (indexExclList.size == snapshot.childrenCount.toInt()) {
                    getExclData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getBestIndex() {
        skeletonLoading.visibility = View.VISIBLE
        listBestSelling?.clear()
        databaseRefBestSelling.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val index = data.value.toString()
                    indexBestList.add(index)
                }

                if (indexBestList.size == snapshot.childrenCount.toInt()) {
                    getBestData()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getExclData() {
        pBExclOffer.visibility = View.VISIBLE
        databaseRefProduct.child(indexExclList[0])
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val exclData = snapshot.getValue(ExclusiveOffer::class.java)
                    listExclOffer?.add(exclData!!)

                    if (indexExclList.isNotEmpty()) {
                        indexExclList.removeAt(0)
                    }

                    if (indexExclList.isEmpty()) {
                        adapterExcl?.notifyDataSetChanged()
                        pBExclOffer.visibility = View.GONE

//                        if (isAdded == true) {
//                            Toast.makeText(
//                                context?.applicationContext,
//                                "Added to Cart Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (isAdded == false) {
//                            Toast.makeText(
//                                context?.applicationContext,
//                                "Removed From Cart to Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }

                        skeletonLoading.visibility = View.GONE
                    } else {
                        getExclData()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getBestData() {
        pBBestSelling.visibility = View.VISIBLE
        databaseRefProduct.child(indexBestList[0])
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val bestData = snapshot.getValue(BestSelling::class.java)
                    listBestSelling?.add(bestData!!)

                    if (indexBestList.isNotEmpty()) {
                        indexBestList.removeAt(0)
                    }

                    if (indexBestList.isEmpty()) {
                        adapterBest?.notifyDataSetChanged()
                        pBBestSelling.visibility = View.GONE

//                        if (isAdded == true) {
//                            Toast.makeText(
//                                context?.applicationContext,
//                                "Added to Cart Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (isAdded == false) {
//                            Toast.makeText(
//                                context?.applicationContext,
//                                "Removed From Cart to Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }

                        skeletonLoading.visibility = View.GONE
                    } else {
                        getBestData()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getData() {

        getExclIndex()

        getBestIndex()

    }

    private fun initVar(view: View) {
        recyclerViewExclOffer = view.findViewById(R.id.recyclerViewExclOffer)
        recyclerViewBestSelling = view.findViewById(R.id.recyclerViewBestSelling)
        seeAllTextViewBest = view.findViewById(R.id.seeAllTextViewBest)
        seeAllTextViewExcl = view.findViewById(R.id.seeAllTextViewExcl)

        pBExclOffer = view.findViewById(R.id.pBExclOffer)
        pBBestSelling = view.findViewById(R.id.pBBestSelling)
        pBLoading = view.findViewById(R.id.pBLoading)
        skeletonLoading = view.findViewById(R.id.skeletonLoading)

        isAdded = null

        databaseRefExclOffer = FirebaseDatabase.getInstance().getReference("ExclusiveOffer")
        databaseRefBestSelling = FirebaseDatabase.getInstance().getReference("BestSelling")
        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

        sharedPreferences =
            requireActivity().getSharedPreferences("GroceriesApp", AppCompatActivity.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

    }

    override fun onExclClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_shopFragment_to_productDetailsFragment2, bundle)
    }

    override fun onBestClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_shopFragment_to_productDetailsFragment2, bundle)
    }

    override fun onAddToCartBestClicked(id: String, addButtonImage: ImageView) {
//        skeletonLoading.visibility = View.VISIBLE

        pBBestSelling.visibility = View.VISIBLE
        databaseRefProduct.child(id).child("Added")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartAddValue = snapshot.value
                    if (cartAddValue == false) {
                        databaseRefAddToCart.child(id).setValue(id)
                            .addOnSuccessListener {
                                databaseRefProduct.child(id).child("Added").setValue(true)
                                    .addOnSuccessListener {
                                        addButtonImage.setImageResource(R.drawable.tickmark_icon)
//                                        getBestIndex()
                                        Toast.makeText(
                                            context?.applicationContext,
                                            "Added to Cart Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        pBBestSelling.visibility = View.GONE
                                    }
                            }
                    } else if (cartAddValue == true) {
                        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
                            databaseRefProduct.child(id).child("Added").setValue(false)
                                .addOnSuccessListener {
                                    addButtonImage.setImageResource(R.drawable.plus_image)
//                                    getBestIndex()
                                    Toast.makeText(
                                        context?.applicationContext,
                                        "Removed From Cart to Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    pBBestSelling.visibility = View.GONE
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onAddToCartExclClicked(id: String, addButtonImage: ImageView) {
//        skeletonLoading.visibility = View.VISIBLE
pBExclOffer.visibility = View.VISIBLE
        databaseRefProduct.child(id).child("Added")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartAddValue = snapshot.value
                    if (cartAddValue == false) {

                        databaseRefAddToCart.child(id).setValue(id).addOnSuccessListener {
                            databaseRefProduct.child(id).child("Added").setValue(true)
                                .addOnSuccessListener {
                                    addButtonImage.setImageResource(R.drawable.tickmark_icon)
//                                    getExclIndex()
                                    Toast.makeText(
                                        context?.applicationContext,
                                        "Added to Cart Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    pBExclOffer.visibility = View.GONE
                                }
                        }
                    } else if (cartAddValue == true) {
                        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
                            databaseRefProduct.child(id).child("Added").setValue(false)
                                .addOnSuccessListener {
                                    addButtonImage.setImageResource(R.drawable.plus_image)
//                                    getExclIndex()
                                    Toast.makeText(
                                        context?.applicationContext,
                                        "Removed From Cart to Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    pBExclOffer.visibility = View.GONE
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