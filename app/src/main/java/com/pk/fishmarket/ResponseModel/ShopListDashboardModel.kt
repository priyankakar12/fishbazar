package com.pk.fishmarket.ResponseModel

data class ShopListDashboardModel(var status:Int,var message:String,var cart_total:String,var SHOP_LIST:ArrayList<ShopList>)
data class ShopList(var SHOP_ID: String,var SHOP_NAME:String,var SHOP_OWNER_NAME:String,var SHOP_IMAGE:String
,var SHOP_ADDRESS:String,var SHOP_PHONE:String,var IMAGE_PATH:String,var DISTANCE:String,var FAVOURITE:Int,var AVRAGERATING :String?)
