package com.pk.fishmarket.ResponseModel

data class Cart_Response_Model(var status: Int,var message:String,var CART_DETAILS: ArrayList<CartDetails>,var SUBTOTAL: String)
data class CartDetails(var PRODUCT_ID: String,var SHOP_ID :String,var SHOP_NAME:String,var PRODUCT_TITLE:String,
                       var PRODUCT_IMAGE:String,
                       var PRODUCT_QUANTITY:String,
                       var PRODUCT_PRICE: String,var SHOP_DISTANCE:String,var QUANTITY_AMOUNT:String,var BASE_PRICE:String,var BASE_AMOUNT:String)
