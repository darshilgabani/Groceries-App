package com.biz.evaluation3groceriesapp.clicklistener

import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.modelclass.Cart

interface OrderListClickListener {
    fun onItemClicked(id : String)
}