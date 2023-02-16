package com.biz.evaluation3groceriesapp.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.BestSellingAdapter
import com.biz.evaluation3groceriesapp.adapter.ExclusiveOfferAdapter
import com.biz.evaluation3groceriesapp.clicklistener.ShopClickListener
import com.biz.evaluation3groceriesapp.modelclass.*
import com.biz.evaluation3groceriesapp.utils.addToCart
import com.google.firebase.database.*

class ShopFragment : Fragment(), ShopClickListener {

    private var cartAddValue: Any? = null

    lateinit var recyclerViewExclOffer: RecyclerView
    private lateinit var recyclerViewBestSelling: RecyclerView
    private lateinit var seeAllTextViewBest: TextView
    private lateinit var seeAllTextViewExcl: TextView

    lateinit var pBExclOffer: ProgressBar
    lateinit var pBBestSelling: ProgressBar
    lateinit var pBLoading: ProgressBar

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

    var exclIndex: ArrayList<String>? = ArrayList()
    var bestIndex: ArrayList<String>? = ArrayList()

    var added: Boolean? = null
    var user: String? = null
    var exclData: ExclusiveOffer? = null

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

    private fun onClick() {
        seeAllTextViewExcl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ClickedID", "20017")
            bundle.putString("ClickedName", "Exclusive Offer")
            Navigation.findNavController(requireView()).navigate(R.id.action_shopFragment_to_categoriesProductFragment, bundle)
        }

        seeAllTextViewBest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ClickedID", "20018")
            bundle.putString("ClickedName", "Best Selling")
            Navigation.findNavController(requireView()).navigate(R.id.action_shopFragment_to_categoriesProductFragment, bundle)
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

//    private fun asdad() {
//        if (listIndex != null) {
//            listExclOffer?.clear()
//            for (product in listIndex!!) {
//
//                Log.d("@@@I", listIndex?.size.toString())
//
//                databaseRefProduct.child(product)
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//
//                            exclData = snapshot.getValue(ExclusiveOffer::class.java)
//
////                            if (exclData != null) {
////                                listExclOffer?.add(exclData)
////                                adapterExcl?.notifyDataSetChanged()
////                                pBExclOffer.visibility = View.GONE
////                            }
//
//                                listExclOffer?.add(exclData!!)
//                            if (product.length - 1 == product.lastIndex) {
//                                Log.d("@@@S", listExclOffer?.size.toString())
//
//
//                                adapterExcl()
////                                adapterExcl?.notifyDataSetChanged()
//                                pBExclOffer.visibility = View.GONE
//                            }
//
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                    })
//            }
//        }
//    }

    private fun getData() {

        listExclOffer?.clear()
        databaseRefExclOffer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    val user = data.value.toString()
//                    listIndex?.add(user!!)
                    databaseRefProduct.child(user)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                Log.d("@@@@", snapshot.value.toString())

                                val exclData = snapshot.getValue(ExclusiveOffer::class.java)

                                if (exclData != null) {
                                    listExclOffer?.add(exclData)
                                    listExclOffer?.sortBy {
                                        it.Id
                                    }
                                    adapterExcl?.notifyDataSetChanged()
                                    pBExclOffer.visibility = View.GONE
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        pBExclOffer.visibility = View.VISIBLE

        listBestSelling?.clear()
        databaseRefBestSelling.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    val user = data.value.toString()
//                    listIndex?.add(user!!)
                    databaseRefProduct.child(user)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                Log.d("@@@@", snapshot.value.toString())

                                val bestData = snapshot.getValue(BestSelling::class.java)

                                if (bestData != null) {
                                    listBestSelling?.add(bestData)
                                    listBestSelling?.sortBy {
                                        it.Id
                                    }
                                    adapterBest?.notifyDataSetChanged()
                                    pBBestSelling.visibility = View.GONE
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        pBBestSelling.visibility = View.VISIBLE

//        println(listIndex)
//
//        if (listIndex != null) {
//            for (product in listIndex!!) {
//                println(product)
//            }
//        }


//        databaseRefProduct.child(user).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                Log.d("@@@@", snapshot.value.toString())
//
//                val exclData = snapshot.getValue(ExclusiveOffer::class.java)
//
//                if (exclData != null) {
//                    listExclOffer?.add(exclData)
//                    listExclOffer?.sortBy {
//                        it.Id
//                    }
//                    adapterExcl?.notifyDataSetChanged()
//                    pBExclOffer.visibility = View.GONE
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })


//        listBestSelling?.clear()
//        databaseRefBestSelling.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
////                bestIndex?.clear()
//                for (data in snapshot.children) {
//                    val user = data.value.toString()
//
//
//                    val lastElement = snapshot.children.last().value
//
//                    databaseRefProduct.child(user)
//                        .addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//
//                                val bestData = snapshot.getValue(BestSelling::class.java)
//
//                                if (bestData != null) {
//                                    listBestSelling?.add(bestData)
//                                    listBestSelling?.sortBy { it.Id }
//                                    pBBestSelling.visibility = View.GONE
//                                }
//
//                                if (lastElement == snapshot.key) {
//                                    adapterBest()
//                                }
//
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                                TODO("Not yet implemented")
//                            }
//                        })
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//        pBBestSelling.visibility = View.VISIBLE


    }

    private fun initVar(view: View) {
        recyclerViewExclOffer = view.findViewById(R.id.recyclerViewExclOffer)
        recyclerViewBestSelling = view.findViewById(R.id.recyclerViewBestSelling)
        seeAllTextViewBest = view.findViewById(R.id.seeAllTextViewBest)
        seeAllTextViewExcl = view.findViewById(R.id.seeAllTextViewExcl)

        pBExclOffer = view.findViewById(R.id.pBExclOffer)
        pBBestSelling = view.findViewById(R.id.pBBestSelling)
        pBLoading = view.findViewById(R.id.pBLoading)

        databaseRefExclOffer = FirebaseDatabase.getInstance().getReference("ExclusiveOffer")
        databaseRefBestSelling = FirebaseDatabase.getInstance().getReference("BestSelling")
        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

        sharedPreferences =
            requireActivity().getSharedPreferences("Login Data", AppCompatActivity.MODE_PRIVATE)
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
        pBLoading.visibility = View.VISIBLE
        addToCart(id, addButtonImage,requireContext(),pBLoading)
    }

    override fun onAddToCartExclClicked(id: String, addButtonImage: ImageView) {
        pBLoading.visibility = View.VISIBLE
        addToCart(id, addButtonImage,requireContext(),pBLoading)
    }

//    private fun addToCart(id: String, addButtonImage: ImageView) {
//
//        databaseRefProduct.child(id).child("Added")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    cartAddValue = snapshot.value
//                    if (cartAddValue == false) {
//                        databaseRefAddToCart.child(id).setValue(id)
//                            .addOnSuccessListener {
//                                addButtonImage.setImageResource(R.drawable.tickmark_icon)
//                                databaseRefProduct.child(id).child("Added").setValue(true)
//                                pBLoading.visibility = View.INVISIBLE
//                                Toast.makeText(
//                                    requireContext().applicationContext,
//                                    "Added to Cart Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                    } else if (cartAddValue == true) {
//                        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
//                            addButtonImage.setImageResource(R.drawable.plus_image)
//                            databaseRefProduct.child(id).child("Added").setValue(false)
//                            pBLoading.visibility = View.INVISIBLE
//                            Toast.makeText(
//                                requireContext().applicationContext,
//                                "Removed From Cart to Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//
//    }

}