package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.OrderDetailAdapter
import com.biz.evaluation3groceriesapp.modelclass.OrderDetail
import com.google.firebase.database.*


class OrderDetailsFragment : Fragment() {
    lateinit var orderDetailRecyclerView: RecyclerView
    lateinit var orderTitle: TextView
    lateinit var itemTotalTextView: TextView
    lateinit var discountTextView: TextView
    lateinit var grandTotalTextView: TextView
    lateinit var backButton: ImageView
    lateinit var orderDetailProgressBar: ProgressBar

    lateinit var skeletonLoading: LottieAnimationView

    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefOrder: DatabaseReference

    private var adapterOrderDetail: OrderDetailAdapter? = null
    var listOrderDetail: ArrayList<OrderDetail>? = ArrayList()

    var clickedId: String? = null
    var itemTotal: String? = null
    var grandTotal: String? = null
    var discountPrice: String? = null

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
            Navigation.findNavController(requireView()).navigateUp()
        }
    }


    private fun getData() {
        skeletonLoading.visibility = View.VISIBLE
        databaseRefOrder.child(clickedId.toString()).child("Product")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val id = data.child("Id").value
                        val count = data.child("Count").value.toString()

                        databaseRefProduct.child(id.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot1: DataSnapshot) {

                                    val name = snapshot1.child("Name").value.toString()
                                    val price = snapshot1.child("Price").value.toString()

                                    listOrderDetail?.add(OrderDetail(name, price, count))

                                    if (listOrderDetail?.size == snapshot.childrenCount.toInt()) {
                                        orderDetailProgressBar.visibility = View.GONE
                                        adapterOrderDetail?.notifyDataSetChanged()

                                        itemTotalTextView.text = "$$itemTotal"

                                        if (discountPrice == "null") {
                                            discountTextView.text = "-$0.0"
                                        } else {
                                            discountTextView.text = "-$$discountPrice"
                                        }

                                        if (grandTotal == "null"){
                                            grandTotalTextView.text = "$$itemTotal"
                                        }else{
                                            grandTotalTextView.text = "$$grandTotal"
                                        }

                                        skeletonLoading.visibility = View.GONE
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
        itemTotal = arguments?.getString("itemTotal")
        grandTotal = arguments?.getString("grandTotal")
        discountPrice = arguments?.getString("discountPrice")

        orderDetailRecyclerView = view.findViewById(R.id.orderDetailRecyclerView)
        orderTitle = view.findViewById(R.id.orderTitle)
        backButton = view.findViewById(R.id.backButton)

        itemTotalTextView = view.findViewById(R.id.itemTotalTextView)
        discountTextView = view.findViewById(R.id.discountTextView)
        grandTotalTextView = view.findViewById(R.id.grandTotalTextView)

        orderDetailProgressBar = view.findViewById(R.id.orderDetailProgressBar)

        skeletonLoading = view.findViewById(R.id.skeletonLoading)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefOrder = FirebaseDatabase.getInstance().getReference("Order")

        orderTitle.text = "Order #${clickedId?.drop(1)}"
    }

}