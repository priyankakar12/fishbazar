package com.pk.fishmarket.repository

import com.pk.fishmarket.networking.RetrofitClient
import retrofit2.http.Field


class AppRepository {
    fun userLogin(user_email : String,user_pass:String) = RetrofitClient.apiInterface.userLogin(user_email,user_pass)
    fun userRegister(phonenumber : String,username: String,email: String,user_pass:String,user_con_pass:String,firstname:String,lastname:String
    ) = RetrofitClient.apiInterface.userRegister(phonenumber,username,email,user_pass,user_con_pass,firstname,lastname)

    fun userOtpVerify(phone_number : String,otp:String) = RetrofitClient.apiInterface.userOtpVerify(phone_number,otp)

    fun getDashboardData(userid:String,lat:String,long:String) = RetrofitClient.apiInterface.getDashboardData(userid,lat,long)

    fun getShopsData(shop_id:String) = RetrofitClient.apiInterface.getShopsData(shop_id)
    fun getSearchedItems(search_item:String) = RetrofitClient.apiInterface.searchItem(search_item)
    fun addReview(rating_count: String,feedback: String,userid: String,shopid: String) = RetrofitClient.apiInterface.addRating(rating_count,feedback,userid,shopid)
    fun addToCart(productid: String,shopid: String,product_quantity: String,userid: String,price:String,status:String,quantity_amount:String) =
        RetrofitClient.apiInterface.addtocart(productid,shopid,product_quantity,userid,price,status,quantity_amount)

    fun getDetails(productid: String) =
        RetrofitClient.apiInterface.getProductDetails(productid)

    fun getCartDetails(userid: String,lat:String,long:String) =
        RetrofitClient.apiInterface.getCartDetails(userid,lat,long)

    fun updateCart(productid: String,product_quantity: String,userid: String,price:String) =
        RetrofitClient.apiInterface.updatecart(productid,product_quantity,userid,price)

    fun deleteCart(productid: String,userid: String) =
        RetrofitClient.apiInterface.deletecart(productid,userid)

    fun addnewAddress(userid: String,name: String,address_one: String,address_two:String,pincode:String,phone_number:String) =
        RetrofitClient.apiInterface.addAddress(userid,name,address_one,address_two,pincode,phone_number)

 fun updateAddress(userid: String,name: String,address_one: String,address_two:String,pincode:String,phone_number:String,addressid:String) =
        RetrofitClient.apiInterface.updateAddress(userid,name,address_one,address_two,pincode,phone_number,addressid)


    fun getAllAddress(userid: String) =
        RetrofitClient.apiInterface.getAddress(userid)

    fun addtoFavour(userid: String,shopid:String,favourite:String) =
        RetrofitClient.apiInterface.addtofavour(userid,shopid,favourite)

    fun searchShop(shop_name: String,userid:String,lat:String,long:String) =
        RetrofitClient.apiInterface.searchShop(shop_name,userid,lat,long)



        fun placeOrder(userid: String,product: String,total_price: String,mode_of_payment:String
                      ,orderdate:String,
                      transaction_id:String,
                      deliverydate:String,
                      addressid:String,delivary_status:String) =
        RetrofitClient.apiInterface.placeOrder(userid,product,total_price,mode_of_payment,transaction_id,orderdate,deliverydate,
            addressid,delivary_status)

        fun orderHistory(userid: String) =
        RetrofitClient.apiInterface.orderHistory(userid)

        fun orderDetails(userid: String,Orderid:String) =
        RetrofitClient.apiInterface.orderDetails(userid,Orderid)

        fun cancelOrder(userid: String,Orderid:String) =
        RetrofitClient.apiInterface.cancelOrder(userid,Orderid)

        fun getUserDetails(userid: String) =
        RetrofitClient.apiInterface.getUserDetails(userid)

        fun getUserProfileUpdate(email_address:String,fullname:String,userid: String) =
        RetrofitClient.apiInterface.getUserProfileUpdate(email_address,fullname,userid)

         fun getFavourites(userid: String) =
        RetrofitClient.apiInterface.getFavourites(userid)

         fun deleteFavourites(userid:String,shopid:String) =
        RetrofitClient.apiInterface.deleteFavourites(userid,shopid)

         fun deleteAddress(userid:String,addressid:String) =
        RetrofitClient.apiInterface.deleteAddress(userid,addressid)






}