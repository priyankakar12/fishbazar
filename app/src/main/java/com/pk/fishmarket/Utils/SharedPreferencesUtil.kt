package com.pk.fishmarket.Utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesUtil {
    private var sp: SharedPreferences? = null

    fun read(context: Context): Boolean {
        val value: Boolean?
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        value = sp!!.getBoolean("login", false)
        return value
    }

    fun write(context: Context, login: Boolean) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putBoolean("login", login)
        editor.apply()
    }

    fun clear(context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("user_id", "")


        editor.clear()
        editor.apply()

        write(context, false)
    }

//    fun setUser(userDetails: UserDetails, context: Context) {
//        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//        val editor = sp!!.edit()
//        editor.putString("userDetails", userDetails.toString())
//        editor.apply()
//    }
//
//    fun getUser(context: Context): UserDetails {
//        sp = context.getSharedPreferences("MyPref", 0)
//        return Gson().fromJson(sp!!.getString("userDetails", ""), UserDetails::class.java)
//    }

    fun setUserId(user_id: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("user_id", user_id)
        editor.apply()
    }

    fun getUserId(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("user_id", "")
    }

    fun saveCountry(country: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("country", country)
        editor.apply()
    }

    fun getCountry(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("country", "")
    }

    fun saveAddress(address: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("address", address)
        editor.apply()
    }

    fun getAddress(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("address", "")
    }

    fun saveState(state: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("state",state)
        editor.apply()
    }

    fun getState(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("state", "")
    }


  fun saveCity(city: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("city",city)
        editor.apply()
    }

    fun getCity(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("city", "")
    }


  fun savePostalCode(postalCode: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("postalCode",postalCode)
        editor.apply()
    }

    fun getPostalCode(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("postalCode", "")
    }


  fun saveLat(lat: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("lat",lat)
        editor.apply()
    }

    fun getLat(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("lat", "")
    }


  fun saveLong(long: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("long",long)
        editor.apply()
    }

    fun getLong(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("long", "")
    }



 fun saveCartCount(cart_total: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("cart_total",cart_total)
        editor.apply()
    }

    fun getCartCount(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("cart_total", "")
    }



    fun savePhone(phone_number: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("phone_number",phone_number)
        editor.apply()
    }

    fun getPhone(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("phone_number", "")
    }

    fun saveEmail(email: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("email",email)
        editor.apply()
    }

    fun getEmail(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("email", "")
    }








}