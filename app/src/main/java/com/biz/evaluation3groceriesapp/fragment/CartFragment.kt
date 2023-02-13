package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.BestSellingAdapter
import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CartClickListener
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.google.firebase.database.*

class CartFragment : Fragment() , CartClickListener {

    private lateinit var cartRecyclerView : RecyclerView
    private lateinit var cartProgressBar : ProgressBar

    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference

    var listCart : ArrayList<Cart>? = ArrayList()

    private var adapterCart :CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        initVar(view)

        getData()

        setLayout()

        return view
    }

    private fun getData() {
        databaseRefAddToCart.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.childrenCount

                for(data in snapshot.children){
                    val user = data.value.toString()

                    val lastElement = snapshot.children.last().value

                    databaseRefProduct.child(user).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val cartData = snapshot.getValue(Cart::class.java)

                            if (cartData!=null){
                                listCart?.add(cartData)
                                cartProgressBar.visibility = View.GONE
                            }

                            if (lastElement == snapshot.key){
                                adapterCart()
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
    }

    private fun setLayout() {
//        cartRecyclerView.layoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
        cartRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)
    }

    private fun adapterCart() {
        adapterCart = CartAdapter(listCart!!)
//        adapterBest?.setSelectListener(this)
        cartRecyclerView.adapter = adapterCart
    }

    private fun initVar(view: View) {
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartProgressBar = view.findViewById(R.id.cartProgressBar)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

    }

    override fun onCloseButtonClicked(id: String) {
        cartProgressBar.visibility = View.VISIBLE
        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
            databaseRefProduct.child(id).child("Added").setValue(false)
            cartProgressBar.visibility = View.INVISIBLE
            Toast.makeText(requireContext().applicationContext, "Removed From Cart to Successfully", Toast.LENGTH_SHORT).show()
        }
    }

}