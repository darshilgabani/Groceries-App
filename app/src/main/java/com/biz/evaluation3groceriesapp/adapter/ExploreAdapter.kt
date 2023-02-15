package com.biz.evaluation3groceriesapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.clicklistener.ExploreClickListener
import com.biz.evaluation3groceriesapp.modelclass.Explore
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView


class ExploreAdapter(private val products: ArrayList<Explore>) :
    RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    var listener: ExploreClickListener? = null

    fun setSelectListener(listener: ExploreClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImageView = itemView.findViewById<ImageView>(R.id.productImageView)
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var cardView = itemView.findViewById<MaterialCardView>(R.id.cardView)


        fun onBindData(data: Explore) {
            nameTextView.text = data.Name

            Glide.with(itemView.context)
                .load(data.Url)
                .placeholder(R.drawable.app_logo)
                .into(productImageView)

            cardView.setCardBackgroundColor(Color.parseColor(data.BgColor.toString()))
            cardView.strokeColor = Color.parseColor(data.BdColor.toString())

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_product_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.onBindData(data)

        holder.cardView.setOnClickListener {
            listener?.onItemClicked(data.Id)
        }

    }

}