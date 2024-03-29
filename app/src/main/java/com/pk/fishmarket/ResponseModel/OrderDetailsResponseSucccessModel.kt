package com.pk.fishmarket.ResponseModel



data class OrderDetailsResponseSucccessModel(var status:Int,var message:String,var ORDER_DETAILS:ArrayList<OrderDetailsSuccess>)
data class OrderDetailsSuccess(var ORDER_ID:String,var PRODUCT_DETAILS:ArrayList<Product_Details_Success>,var TOTAL_PRICE:String
,var PAYMENT_MODE:String,var ORDER_DATE:String,var DELIVARY_DATE:String,var USER_NAME:String,var USER_EMAIL:String,
                               var USER_PHONE:String,var ADDRESS_DETAILS:ArrayList<AddressDetailsModel>,var ORDER_STATUS:String,var DELIVARY_STATUS:String)
data class Product_Details_Success(var PRODUCT_PRICE:String,var PRODUCT_NAME:String,var PRODUCT_QUANTITY:String,var PRODUCT_IMAGE:String,var QUANTITY_AMOUNT:String)
data class AddressDetailsModel(var NAME:String,var ADDRESS_ONE:String,var ADDRESS_TWO:String,var PINCODE:String)
