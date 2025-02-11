package com.biz.evaluation3groceriesapp.clicklistener

import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.modelclass.Cart

interface CartClickListener {
    fun onCloseButtonClicked(data : Cart)
    fun onPlusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder, position: Int)
    fun onMinusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder, position: Int)
}