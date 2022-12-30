package com.pk.fishmarket.ResponseModel.FavouriteItems

data class FavouriteItemsMainModel(
    val ORDER_DETAILS: ArrayList<ORDERDETAILS>,
    val message: String,
    val status: Int
)