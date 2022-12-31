package com.pk.fishmarket.ResponseModel

data class AddressDetailsResponseModel(var status:Int,var message:String,var ADDRESS_DETAILS:ArrayList<AddressDetails>)
data class AddressDetails(var ADDRESS_ID:String,var USER_ID:Int,var NAME:String,var ADDRESS_ONE:String,var ADDRESS_TWO:String,var PINCODE:String,var PHONE:String)
