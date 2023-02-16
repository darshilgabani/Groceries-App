package com.biz.evaluation3groceriesapp.clicklistener

import android.widget.ImageView

interface CategoriesClickListener {
    fun onItemClicked(id: String)
    fun onAddToCartClicked(id : String, addButtonImage: ImageView)
}