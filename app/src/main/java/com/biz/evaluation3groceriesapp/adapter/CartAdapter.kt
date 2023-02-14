package com.biz.evaluation3groceriesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.clicklistener.CartClickListener
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.bumptech.glide.Glide

class CartAdapter(private val products: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var listener: CartClickListener? = null
    private var count: Int? = null
    private var totalPrice: Double = 0.0

    fun setSelectListener(listener: CartClickListener) {
        this.listener = listener
    }

    fun getItemList(): ArrayList<Cart> {
        return products
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImageView = itemView.findViewById<ImageView>(R.id.productImageView)
        var minusImageView = itemView.findViewById<ImageView>(R.id.minusImageView)
        var closeButton = itemView.findViewById<ImageView>(R.id.closeButton)
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var weightTextView = itemView.findViewById<TextView>(R.id.weightTextView)
        var priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
        var countTextView = itemView.findViewById<TextView>(R.id.countTexView)
        var plusButton = itemView.findViewById<CardView>(R.id.plusButton)
        var minusButton = itemView.findViewById<CardView>(R.id.minusButton)

        var totalPrice: Double = 0.0

        fun onBindData(data: Cart) {
            nameTextView.text = data.Name
            weightTextView.text = data.Weight
//            priceTextView.text = data.Price
            countTextView.text = data.ItemCount.toString()

            val timesPrice = data.Price.drop(1).toDouble().times(data.ItemCount)
            val formattedPrice = String.format("%.2f", timesPrice)
            priceTextView.text = "$$formattedPrice"

            Glide.with(itemView.context)
                .load(data.Url)
                .placeholder(R.drawable.app_logo)
                .into(productImageView)

            if (data.ItemCount > 1) {
                minusImageView.setImageResource(R.drawable.enabled_minus)
            }

            if (data.ItemCount ==1){
                minusImageView.setImageResource(R.drawable.minus_button)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.mycart_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)

        holder.closeButton.setOnClickListener {
            listener?.onCloseButtonClicked(data.Id)
        }

//        count=0
//        count = data.ItemCount
        holder.plusButton.setOnClickListener {

//            if (count != null) {
//                count = count!! + 1
//
//                timesPrice(data, holder, count!!)
//
//                holder.countTexView.text = count.toString()
//                if (count!! > 1) {
//                    holder.minusImageView.setImageResource(R.drawable.enabled_minus)
//                }
//            }

            listener?.onPlusButtonClicked(data, holder)
        }


        holder.minusButton.setOnClickListener {
//            if (count != null) {
//                if (count!! > 1) {
//                    count = count!! - 1
//
//                    timesPrice(data, holder, count!!)
//
//                    holder.countTextView.text = count.toString()
//                }
//                if (count == 1) {
//                    holder.minusImageView.setImageResource(R.drawable.minus_button)
//                }
//            }
            listener?.onMinusButtonClicked(data, holder)
        }

    }

    private fun timesPrice(data: Cart, holder: ViewHolder, count: Int) {
        val timesPrice = data.Price.drop(1).toDouble().times(count)
        val formattedPrice = String.format("%.2f", timesPrice)
        holder.priceTextView.text = "$$formattedPrice"
    }

}