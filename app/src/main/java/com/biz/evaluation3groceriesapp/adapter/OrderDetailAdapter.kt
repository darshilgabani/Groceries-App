package com.biz.evaluation3groceriesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.biz.evaluation3groceriesapp.modelclass.OrderDetail

class OrderDetailAdapter(private val products: ArrayList<OrderDetail>) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var countTextView = itemView.findViewById<TextView>(R.id.countTextView)
        var priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)

        fun onBindData(data: OrderDetail) {
            nameTextView.text = data.Name
            countTextView.text = data.ItemCount

            val timesPrice = data.Price.drop(1).toDouble().times(data.ItemCount.toInt())
            val formattedPrice = String.format("%.2f", timesPrice)
            priceTextView.text = "$$formattedPrice"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orderdetails_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)
    }

}