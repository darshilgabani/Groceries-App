package com.biz.evaluation3groceriesapp.modelclass

data class LoginCredentialsData(
    var email : String = "",
    var password : String = ""
)

data class ExclusiveOffer(
    var Id:String = "",
    var Added:Boolean? = null,
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = ""
)

data class ProductDetails(
    var Id:String = "",
    var Added:Boolean? = null,
    var FavAdded:Boolean? = null,
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = "",
    var ItemCount: Int = 0
)

data class BestSelling(
    var Id:String = "",
    var Added:Boolean? = null,
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = ""
)

data class AddCart(
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var Details: String = "",
    var Rating: String = "",
    var Nutrition: String = ""
)

data class Cart(
    var Id:String = "",
    var Added:Boolean? = null,
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var ItemCount: Int = 0
)