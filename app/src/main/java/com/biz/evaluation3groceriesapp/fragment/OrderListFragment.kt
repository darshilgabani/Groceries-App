package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.FavouriteAdapter
import com.biz.evaluation3groceriesapp.adapter.OrderListAdapter
import com.biz.evaluation3groceriesapp.clicklistener.OrderListClickListener
import com.biz.evaluation3groceriesapp.modelclass.Favourite
import com.biz.evaluation3groceriesapp.modelclass.Order
import com.google.firebase.database.*


class OrderListFragment : Fragment(),OrderListClickListener {
    lateinit var orderRecyclerView: RecyclerView
    lateinit var emptyOrderTextView: TextView
    lateinit var orderProgressBar: ProgressBar

    var listOrderId: ArrayList<Order>? = ArrayList()

    private lateinit var databaseRefOrder: DatabaseReference

    private var adapterOrder: OrderListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orderlist, container, false)

        initVar(view)

        getData()

        setLayout()

        adapterOrder()

        return view
    }

    private fun adapterOrder() {
        adapterOrder = OrderListAdapter(listOrderId!!)
        adapterOrder?.setSelectListener(this)
        orderRecyclerView.adapter = adapterOrder
    }

    private fun setLayout() {
        orderRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)
    }

    private fun getData() {
        databaseRefOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    if (isAdded) {
                        emptyOrderTextView.visibility = View.VISIBLE
                    }
                }else{
                    orderProgressBar.visibility = View.VISIBLE
//                    listFav?.clear()
                    for (data in snapshot.children) {

                        val keys = data.key.toString()

                        listOrderId?.add(Order(keys))

                        if (listOrderId?.size == snapshot.childrenCount.toInt()){
                            orderProgressBar.visibility = View.GONE
                            adapterOrder?.notifyDataSetChanged()
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun initVar(view: View) {
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView)
        emptyOrderTextView = view.findViewById(R.id.emptyOrderTextView)
        orderProgressBar = view.findViewById(R.id.orderProgressBar)

        databaseRefOrder = FirebaseDatabase.getInstance().reference.child("Order")

    }

    override fun onItemClicked(id: String) {
        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show()
    }


}