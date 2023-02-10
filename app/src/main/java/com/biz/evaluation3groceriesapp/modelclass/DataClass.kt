package com.biz.evaluation3groceriesapp.modelclass

data class LoginCredentialsData(
    var email : String = "",
    var password : String = ""
)

data class ExclusiveOffer(
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = ""
)

data class BestSelling(
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = ""
)

data class Cart(
    var CartAdded: Boolean? = null
)