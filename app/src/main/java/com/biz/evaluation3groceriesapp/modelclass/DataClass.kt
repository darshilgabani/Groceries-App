package com.biz.evaluation3groceriesapp.modelclass

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

data class Cart(
    var Id:String = "",
    var Added:Boolean? = null,
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
    var ItemCount: Int = 0
)

data class Favourite(
    var Id:String = "",
    var Name: String = "",
    var Price: String = "",
    var Url: String = "",
    var Weight: String = "",
)

data class Explore(
    var Id:String = "",
    var Name: String = "",
    var Url: String = "",
    var BgColor: String = "",
    var BdColor: String = "",
)

data class Categories(
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

data class Order(
    var Id:String = "",
    var Time:String = "",
    var Date:String = "",
    var ItemTotal:String = "",
    var GrandTotal:String = "",
    var DiscountPrice:String = "",
)

data class OrderDetail(
    var Name: String = "",
    var Price: String = "",
    var ItemCount: String = ""
)