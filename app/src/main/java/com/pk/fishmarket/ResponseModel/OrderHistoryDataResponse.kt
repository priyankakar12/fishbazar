package com.pk.fishmarket.ResponseModel


data class OrderHistoryDataResponse(var status:Int,var message:String,var ORDER_DETAILS:ArrayList<OrderDetails>)
data class OrderDetails(var USER_ID:String,var ORDER_ID:String,var PRODUCT_DETAILS:ArrayList<PRODUCT_DETAILS>,
                        var TOTAL_PRICE:String,var PAYMENT_MODE:String,var ORDER_DATE:String,var DELIVARY_DATE:String,var USER_NAME:String,
                        var USER_EMAIL:String,var USER_PHONE:String)

data class PRODUCT_DETAILS(var shop_id:String,var product_price:String,var product_name:String,
                        var product_quantity:String)

