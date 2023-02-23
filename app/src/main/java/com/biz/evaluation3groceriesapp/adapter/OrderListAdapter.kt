package com.biz.evaluation3groceriesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.clicklistener.OrderListClickListener
import com.biz.evaluation3groceriesapp.modelclass.Order

class OrderListAdapter(private val products: ArrayList<Order>) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    var listener: OrderListClickListener? = null

    fun setSelectListener(listener: OrderListClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderIdTextView = itemView.findViewById<TextView>(R.id.orderIdTextView)
        var dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        var timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)
        var orderListLayout = itemView.findViewById<ConstraintLayout>(R.id.orderListLayout)

        fun onBindData(data: Order) {
            orderIdTextView.text = data.Id.drop(1)
            dateTextView.text = data.Date
            timeTextView.text = data.Time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orderlist_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)

        holder.orderListLayout.setOnClickListener {
            listener?.onItemClicked(data)
        }

    }

}