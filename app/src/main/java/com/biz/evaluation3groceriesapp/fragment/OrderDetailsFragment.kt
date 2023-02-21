package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.OrderDetailAdapter
import com.biz.evaluation3groceriesapp.adapter.OrderListAdapter
import com.biz.evaluation3groceriesapp.modelclass.Order
import com.biz.evaluation3groceriesapp.modelclass.OrderDetail
import com.biz.evaluation3groceriesapp.utils.databaseRefProduct
import com.google.firebase.database.*


class OrderDetailsFragment : Fragment() {
    lateinit var orderDetailRecyclerView : RecyclerView
    lateinit var orderTitle : TextView
    lateinit var totalPriceTextView : TextView
    lateinit var backButton : ImageView
    lateinit var orderDetailProgressBar : ProgressBar

    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefOrder: DatabaseReference

    private var formattedPrice: String? = null

    private var adapterOrderDetail: OrderDetailAdapter? = null
    var listOrderDetail: ArrayList<OrderDetail>? = ArrayList()


    var clickedId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orderdetails, container, false)

        initVar(view)

        getData()

        onClick()

        setLayout()

        adapterOrderDetail()

        return view
    }

    private fun onClick() {
        backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_orderDetailsFragment_to_orderListFragment)
        }
    }


    private fun getData() {

        databaseRefOrder.child(clickedId.toString()).child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children){
                    val id = data.child("Id").value
                    val count = data.child("Count").value.toString()

                    databaseRefProduct.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot1: DataSnapshot) {

                                val name = snapshot1.child("Name").value.toString()
                                val price = snapshot1.child("Price").value.toString()

                                listOrderDetail?.add(OrderDetail(name,price,count))

                            if (listOrderDetail?.size == snapshot.childrenCount.toInt()){
                                orderDetailProgressBar.visibility = View.GONE
                                adapterOrderDetail?.notifyDataSetChanged()

                                val itemList = adapterOrderDetail!!.getItemList()
                                var totalPrice = 0.0
                                for (item in itemList) {
                                    totalPrice += item.Price.drop(1).toDouble()
                                        .times(item.ItemCount.toInt())
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun adapterOrderDetail() {
        adapterOrderDetail = OrderDetailAdapter(listOrderDetail!!)
        orderDetailRecyclerView.adapter = adapterOrderDetail
    }

    private fun setLayout() {
        orderDetailRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)
    }

    private fun initVar(view: View) {
        clickedId = arguments?.getString("ClickedID")
        orderDetailRecyclerView = view.findViewById(R.id.orderDetailRecyclerView)
        orderTitle = view.findViewById(R.id.orderTitle)
        backButton = view.findViewById(R.id.backButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        orderDetailProgressBar = view.findViewById(R.id.orderDetailProgressBar)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefOrder = FirebaseDatabase.getInstance().getReference("Order")

        orderTitle.text = "Order #${clickedId?.drop(1)}"
    }

}