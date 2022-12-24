package com.pk.fishmarket.networking

import com.pk.fishmarket.ResponseModel.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {
    @FormUrlEncoded
    @POST("Shop_user/login")
    fun userLogin(@Field("phone_number") phone_number: String): Single<Response<LoginResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/user_signup")
    fun userRegister(
        @Field("phonenumber") phonenumber: String,
        @Field("username") firstname: String,
        @Field("email") email: String
    ): Single<Response<LoginResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/otp_verify")
    fun userOtpVerify(@Field("phone_number") phone_number: String,@Field("otp") otp:String): Single<Response<OtpResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/all_shop")
    fun getDashboardData(@Field("userid") userid: String,@Field("lat") lat:String, @Field("long") long:String): Single<Response<ShopListDashboardModel>>

    @FormUrlEncoded
    @POST("Shop_user/shop_products")
    fun getShopsData(@Field("shop_id") shop_id: String): Single<Response<ShopListResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/rating_feedback")
    fun addRating(
        @Field("rating_count") rating_count: String,
        @Field("feedback") feedback: String,
        @Field("userid") userid: String,
        @Field("shopid") shopid: String
    ): Single<Response<RatingModel>>

    @FormUrlEncoded
    @POST("Shop_user/product_search")
    fun searchItem(@Field("search_item") search_item: String): Single<Response<ShopListResponseModel>>

 @FormUrlEncoded
    @POST("Shop_user/shop_cart")
    fun addtocart(
     @Field("productid") productid: String,
     @Field("shopid") shopid: String,
     @Field("product_quantity") product_quantity: String,
     @Field("userid") userid: String,
     @Field("price") price: String,
     @Field("status") status: String
 ): Single<Response<RatingModel>>

@FormUrlEncoded
    @POST("Shop_user/product_details")
    fun getProductDetails(
     @Field("productid") productid: String,

 ): Single<Response<ProductDetailsResponseModel>>


    @FormUrlEncoded
    @POST("Shop_user/getCartDetails")
    fun getCartDetails(
     @Field("userid") userid: String,
     @Field("Lat") lat:String, @Field("long") long:String

 ): Single<Response<Cart_Response_Model>>


    @FormUrlEncoded
    @POST("Shop_user/cart_update")
    fun updatecart(
        @Field("productid") productid: String,
        @Field("product_quantity") product_quantity: String,
        @Field("userid") userid: String,
        @Field("price") price: String

    ): Single<Response<CartUpdateResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/cart_delete")
    fun deletecart(
        @Field("productid") productid: String,
        @Field("userid") userid: String


    ): Single<Response<CartUpdateResponseModel>>

    @FormUrlEncoded
    @POST("Shop_user/add_address")
    fun addAddress(

        @Field("userid") userid: String,
        @Field("name") name: String,
        @Field("address_one") address_one: String,
        @Field("address_two") address_two: String,
        @Field("pincode") pincode: String,
        @Field("phone_number") phone_number: String

    ): Single<Response<CartUpdateResponseModel>>



    @FormUrlEncoded
    @POST("Shop_user/update_address")
    fun updateAddress(
        @Field("userid") userid: String,
        @Field("name") name: String,
        @Field("address_one") address_one: String,
        @Field("address_two") address_two: String,
        @Field("pincode") pincode: String,
        @Field("phone_number") phone_number: String

    ): Single<Response<CartUpdateResponseModel>>



    @FormUrlEncoded
    @POST("Shop_user/getaddressDetails")
    fun getAddress(

        @Field("userid") userid: String,

    ): Single<Response<AddressDetailsResponseModel>>


 @FormUrlEncoded
    @POST("Shop_user/add_favourite")
    fun addtofavour(

        @Field("userid") userid: String,
        @Field("shopid") shopid: String,
        @Field("favourite") favourite: String

    ): Single<Response<CartUpdateResponseModel>>



    @FormUrlEncoded
    @POST("Shop_user/order_place")
    fun placeOrder(
        @Field("userid") userid: String,
        @Field("product") product: String,
        @Field("total_price") total_price: String,
        @Field("mode_of_payment") mode_of_payment: String,
        @Field("transaction_id") transaction_id: String,
        @Field("orderdate") orderdate: String,
        @Field("deliverydate") deliverydate: String,
        @Field("addressid") addressid: String

    ): Single<Response<CartUpdateResponseModel>>


@FormUrlEncoded
    @POST("Shop_user/shop_search")
    fun searchShop(
        @Field("shop_name") shop_name: String,
        @Field("userid") userid: String,
        @Field("lat") lat: String,
        @Field("long") long: String

    ): Single<Response<ShopListDashboardModel>>




@FormUrlEncoded
    @POST("Shop_user/order_history")
    fun orderHistory(

        @Field("userid") userid: String


    ): Single<Response<OrderHistoryDataResponse>>


@FormUrlEncoded
    @POST("Shop_user/order_details")
    fun orderDetails(

        @Field("userid") userid: String,
        @Field("Orderid") Orderid: String


    ): Single<Response<OrderDetailsResponseSucccessModel>>



    @FormUrlEncoded
    @POST("Shop_user/order_cancel")
    fun cancelOrder(

        @Field("userid") userid: String,
        @Field("Orderid") Orderid: String

    ): Single<Response<CartUpdateResponseModel>>






}