package com.pk.fishmarket.ResponseModel

data class ProductDetailsResponseModel(var status:Int,var PRODUCT_DETAILS:ArrayList<ProductDetails>,var RELATED_PRODUCTS:ArrayList<RelatedProducts>)
data class ProductDetails(var PRODUCT_ID:String,var SHOP_ID:String,var PRODUCT_NAME:String,var PRODUCT_DESC:String,var PRODUCT_PRICE:String,var PRODUCT_STOCK: String,var PRODUCT_IMAGE:String)
data class RelatedProducts(var PRODUCT_ID:String,var SHOP_ID:String,var PRODUCT_NAME:String,var PRODUCT_DESC:String,var PRODUCT_PRICE:String,var PRODUCT_STOCK: String)