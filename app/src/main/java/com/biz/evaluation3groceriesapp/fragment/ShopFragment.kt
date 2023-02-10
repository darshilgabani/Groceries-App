package com.biz.evaluation3groceriesapp.fragment

import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.biz.evaluation3groceriesapp.clicklistener.SelectListener
import com.biz.evaluation3groceriesapp.modelclass.*
import com.google.firebase.database.*
import com.google.gson.Gson

class ShopFragment : Fragment(),SelectListener {

    lateinit var recyclerViewExclOffer : RecyclerView
    private lateinit var recyclerViewBestSelling : RecyclerView

    lateinit var pBExclOffer : ProgressBar
    lateinit var pBBestSelling : ProgressBar
    lateinit var pBLoading : ProgressBar

    private lateinit var databaseRefExclOffer: DatabaseReference
    private lateinit var databaseRefBestSelling: DatabaseReference
    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

     private var adapterBest :BestSellingAdapter? = null
     var adapterExcl :ExclusiveOfferAdapter? = null

    var listExclOffer : ArrayList<ExclusiveOffer>? = ArrayList()
    var listBestSelling : ArrayList<BestSelling>? = ArrayList()

    var added : Boolean? = null

    lateinit var gridLayoutManager : GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        initVar(view)

        getData()

        setLayout()

        return view
    }

    private fun setLayout() {
        gridLayoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
        recyclerViewExclOffer.layoutManager = gridLayoutManager

        recyclerViewBestSelling.layoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
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

    private fun getData() {

        listExclOffer?.clear()
        databaseRefExclOffer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.childrenCount

                for(data in snapshot.children){
                    val user = data.value.toString()

                    val lastElement = snapshot.children.last().value

                    databaseRefProduct.child(user).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val exclData = snapshot.getValue(ExclusiveOffer::class.java)

                            if (exclData!=null){
                                listExclOffer?.add(exclData)
                                pBExclOffer.visibility = View.GONE
                            }

                            if (lastElement == snapshot.key){
                                adapterExcl()
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
                for(data in snapshot.children){
                    val user = data.value.toString()

                    val lastElement = snapshot.children.last().value

                    databaseRefProduct.child(user).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val bestData = snapshot.getValue(BestSelling::class.java)

                            if (bestData!=null){
                                listBestSelling?.add(bestData)
                                pBBestSelling.visibility = View.GONE
                            }

                            if (lastElement == snapshot.key){
                                adapterBest()
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


    }

    private fun initVar(view: View) {
        recyclerViewExclOffer = view.findViewById(R.id.recyclerViewExclOffer)
        recyclerViewBestSelling = view.findViewById(R.id.recyclerViewBestSelling)

        pBExclOffer = view.findViewById(R.id.pBExclOffer)
        pBBestSelling = view.findViewById(R.id.pBBestSelling)
        pBLoading = view.findViewById(R.id.pBLoading)

        databaseRefExclOffer = FirebaseDatabase.getInstance().getReference("ExclusiveOffer")
        databaseRefBestSelling = FirebaseDatabase.getInstance().getReference("BestSelling")
        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

        sharedPreferences = requireActivity().getSharedPreferences("Login Data", AppCompatActivity.MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

    }

    override fun onExclClicked(id : String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        Navigation.findNavController(requireView()).navigate(R.id.action_shopFragment_to_productDetailsFragment2,bundle)
    }

    override fun onBestClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID",id)
        Navigation.findNavController(requireView()).navigate(R.id.action_shopFragment_to_productDetailsFragment2,bundle)
    }

    override fun onAddToCartBestClicked(id: String, addButtonImage: ImageView) {
        pBLoading.visibility = View.VISIBLE
        addToCart(id,addButtonImage)
    }

    override fun onAddToCartExclClicked(id : String, addButtonImage: ImageView) {
        pBLoading.visibility = View.VISIBLE
        addToCart(id,addButtonImage)
    }

    private fun addToCart(id: String, addButtonImage: ImageView) {
        databaseRefAddToCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    databaseRefAddToCart.child(id).setValue(id)
                        .addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.tickmark_icon)
                            databaseRefProduct.child(id).child("Added").setValue(true)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show()
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
                        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.plus_image)
                            databaseRefProduct.child(id).child("Added").setValue(false)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Remove From Cart to Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }else if (added == false){
                        databaseRefAddToCart.child(id).setValue(id)
                            .addOnSuccessListener {
                                addButtonImage.setImageResource(R.drawable.tickmark_icon)
                                databaseRefProduct.child(id).child("Added").setValue(true)
                                pBLoading.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show()
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