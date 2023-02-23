package com.biz.evaluation3groceriesapp.clicklistener

import com.biz.evaluation3groceriesapp.modelclass.Order

interface OrderListClickListener {
    fun onItemClicked(data: Order)
}