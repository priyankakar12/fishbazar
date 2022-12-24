package com.pk.fishmarket.Utils

import android.content.Context
import android.net.ConnectivityManager

object InternetConnection {
    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifiInfo != null && wifiInfo.isConnected || mobileInfo != null && mobileInfo.isConnected
    }
}