package com.biz.evaluation3groceriesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.clicklistener.SelectListener
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.bumptech.glide.Glide

class ExclusiveOfferAdapter(private val products: ArrayList<ExclusiveOffer>) : RecyclerView.Adapter<ExclusiveOfferAdapter.ViewHolder>() {

    var listener : SelectListener? = null

    fun setSelectListener(listener: SelectListener){
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImageView = itemView.findViewById<ImageView>(R.id.productImageView)
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var weightTextView = itemView.findViewById<TextView>(R.id.weightTextView)
        var priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
        var addCartButton = itemView.findViewById<CardView>(R.id.addCartButton)
        var cardView = itemView.findViewById<CardView>(R.id.cardView)
        var addButtonImage = itemView.findViewById<ImageView>(R.id.addButtonImage)

        fun onBindData(data: ExclusiveOffer) {
            nameTextView.text = data.Name
            weightTextView.text = data.Weight
            priceTextView.text = data.Price

            Glide.with(itemView.context)
                .load(data.Url)
                .placeholder(R.drawable.app_logo)
                .into(productImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)

        holder.cardView.setOnClickListener {
            listener?.onExclClicked(data)
        }

        holder.addCartButton.setOnClickListener {
            listener?.onAddToCartExclClicked(data,holder.addButtonImage)
        }

    }

}