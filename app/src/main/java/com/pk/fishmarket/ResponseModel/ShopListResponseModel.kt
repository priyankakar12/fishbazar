package com.pk.fishmarket.ResponseModel

data class ShopListResponseModel(var status:Int,var message:String,var PRODUCT_LIST : ArrayList<ShopResponse>)
data class  ShopResponse(var PRODUCT_ID :String,var SHOP_ID:String,var PRODUCT_NAME:String,var PRODUCT_IMAGE : String,var PRODUCT_PRICE:String,var PRODUCT_STOCK : String)
