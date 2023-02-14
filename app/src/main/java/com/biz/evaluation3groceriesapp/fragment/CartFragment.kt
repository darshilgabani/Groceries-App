package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CartClickListener
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.google.firebase.database.*

class CartFragment : Fragment(), CartClickListener {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartProgressBar: ProgressBar
    private lateinit var checkOutButton: CardView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartTextView: TextView

    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference

    var listCart: ArrayList<Cart>? = ArrayList()

    //    private var count: Int? = null
    private var formattedPrice: String? = null

    private var adapterCart: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)


        initVar(view)

        getData()

        setLayout()

        adapterCart()

        return view
    }

    private fun getData() {
        databaseRefAddToCart.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    if (isAdded) {
                        emptyCartTextView.visibility = View.VISIBLE
                    }
                }else{
                    cartProgressBar.visibility = View.VISIBLE
                    for (data in snapshot.children) {

                        val user = data.value.toString()

                        val lastElement = snapshot.children.last().value

                        databaseRefProduct.child(user).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {



                                val cartData = snapshot.getValue(Cart::class.java)

                                if (cartData != null) {
                                    listCart?.add(cartData)
                                    cartProgressBar.visibility = View.GONE
                                }

                                if (lastElement == snapshot.key) {
                                    adapterCart?.notifyDataSetChanged()

                                    val itemList = adapterCart!!.getItemList()
                                    var totalPrice = 0.0
                                    for (item in itemList) {
                                        totalPrice += item.Price.drop(1).toDouble()
                                            .times(item.ItemCount)
                                    }
                                    formattedPrice = String.format("%.2f", totalPrice)
                                    totalPriceTextView.text = "$ $formattedPrice"

                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                    }
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
        adapterCart?.setSelectListener(this)
        cartRecyclerView.adapter = adapterCart
    }

    private fun initVar(view: View) {
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartProgressBar = view.findViewById(R.id.cartProgressBar)
        checkOutButton = view.findViewById(R.id.checkOutButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        emptyCartTextView = view.findViewById(R.id.emptyCartTextView)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

    }

    override fun onCloseButtonClicked(id: String) {
        cartProgressBar.visibility = View.VISIBLE
        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
            databaseRefProduct.child(id).child("Added").setValue(false)
            listCart?.clear()
//            getData()
            adapterCart?.notifyDataSetChanged()
            cartProgressBar.visibility = View.INVISIBLE
            Toast.makeText(
                requireContext().applicationContext,
                "Removed From Cart to Successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPlusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {

        cartProgressBar.visibility = View.VISIBLE
        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    if ((itemCount as Long).toInt() == 1) {
                        holder.minusImageView.setImageResource(R.drawable.minus_button)
                    }

                    val updatedCount = (itemCount as Long).toInt() + 1

                    cartProgressBar.visibility = View.VISIBLE
                    databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                        .addOnSuccessListener {

                            timesPrice(data, holder, updatedCount)

                            holder.countTextView.text = updatedCount.toString()
                            listCart?.clear()
                            getData()
//                        adapterCart?.notifyDataSetChanged()
//                        adapterCart()
                            cartProgressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Added to Cart Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onMinusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {

        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    if ((itemCount as Long).toInt() == 1) {
                        holder.minusImageView.setImageResource(R.drawable.minus_button)
                    }


                    if ((itemCount as Long).toInt() > 1) {
                        cartProgressBar.visibility = View.VISIBLE

                        val updatedCount = (itemCount as Long).toInt() - 1

                        cartProgressBar.visibility = View.VISIBLE
                        databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                            .addOnSuccessListener {

                                timesPrice(data, holder, updatedCount)

                                holder.countTextView.text = updatedCount.toString()
                                listCart?.clear()
                                getData()
//                        adapterCart?.notifyDataSetChanged()
//                        adapterCart()
                                cartProgressBar.visibility = View.INVISIBLE
                                Toast.makeText(
                                    requireContext(),
                                    "Added to Cart Successfully",
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

    private fun timesPrice(data: Cart, holder: CartAdapter.ViewHolder, count: Int) {
        val timesPrice = data.Price.drop(1).toDouble().times(count)
        val formattedPrice = String.format("%.2f", timesPrice)
        holder.priceTextView.text = "$$formattedPrice"
    }
}



