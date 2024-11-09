package com.example.musicapp.tool.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetWorkConnectionChecker(private val context: Context):INetWorkConnectionChecker {


    override fun isConnectionNormal(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply{
                return  hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            }
        }
        // For below 29 api
        else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.apply{  return isConnectedOrConnecting }

        }
        return false
    }


}