package com.pk.fishmarket.ResponseModel.UserDetails

data class UserDetailResponseModel(
    val PROFILE_DETAILS: List<PROFILEDETAILS>,
    val message: String,
    val status: Int
)