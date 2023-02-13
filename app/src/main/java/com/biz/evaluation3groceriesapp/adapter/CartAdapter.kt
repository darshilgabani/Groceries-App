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

class CartAdapter(private val products: ArrayList<Cart>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var listener : CartClickListener? = null

    fun setSelectListener(listener: CartClickListener){
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImageView = itemView.findViewById<ImageView>(R.id.productImageView)
        var closeButton = itemView.findViewById<ImageView>(R.id.closeButton)
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var weightTextView = itemView.findViewById<TextView>(R.id.weightTextView)
        var priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
        var countTexView = itemView.findViewById<TextView>(R.id.countTexView)
        var plusButton = itemView.findViewById<CardView>(R.id.plusButton)
        var minusButton = itemView.findViewById<CardView>(R.id.minusButton)

        fun onBindData(data: Cart) {
            nameTextView.text = data.Name
            weightTextView.text = data.Weight
            priceTextView.text = data.Price
            countTexView.text = data.ItemCount.toString()

            Glide.with(itemView.context)
                .load(data.Url)
                .placeholder(R.drawable.app_logo)
                .into(productImageView)

//            if (data.Added == false){
//                addButtonImage.setImageResource(R.drawable.plus_image)
//            }else if (data.Added == true){
//                addButtonImage.setImageResource(R.drawable.tickmark_icon)
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mycart_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)

        holder.closeButton.setOnClickListener {
            listener?.onCloseButtonClicked(data.Id)
        }
//
//        holder.addCartButton.setOnClickListener {
//            listener?.onAddToCartExclClicked(data.Id,holder.addButtonImage)
//        }

    }

}