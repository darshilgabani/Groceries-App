package com.biz.evaluation3groceriesapp.clicklistener

import android.widget.ImageView
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer

interface SelectListener {
    fun onExclClicked(model: ExclusiveOffer)
    fun onBestClicked(model: BestSelling)

    fun onAddToCartBestClicked(model: BestSelling, addButtonImage: ImageView)
    fun onAddToCartExclClicked(model: ExclusiveOffer, addButtonImage: ImageView)
}