package com.biz.evaluation3groceriesapp.clicklistener

import android.widget.ImageView

interface ShopClickListener {
    fun onExclClicked(id : String)
    fun onBestClicked(id : String)
    fun onAddToCartBestClicked(id : String, addButtonImage: ImageView)
    fun onAddToCartExclClicked(id : String, addButtonImage: ImageView)
}