package com.biz.evaluation3groceriesapp.clicklistener

import android.widget.ImageView
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer

interface SelectListener {
    fun onExclClicked(id : String)
    fun onBestClicked(id : String)

    fun onAddToCartBestClicked(id : String, addButtonImage: ImageView)
    fun onAddToCartExclClicked(id : String, addButtonImage: ImageView)
}