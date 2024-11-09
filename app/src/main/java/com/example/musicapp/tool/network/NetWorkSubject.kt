package com.example.musicapp.tool.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network



class NetWorkSubject(val context: Context) :Subject<Boolean> {

    private var connectivityManager: ConnectivityManager = context.getSystemService(CONNECTIVITY_SERVICE ) as ConnectivityManager

    private var  connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            notifyObservers(true)
        }

        override fun onLost(network: Network) {
            notifyObservers(false)
        }
    }

    var observers = ArrayList<Observer<Boolean>>()


    override fun AddObserver(o: Observer<Boolean>) {

        observers.add(o)

    }

    override fun removeObserver(o: Observer<Boolean>) {
        observers.remove(o)
    }

    override fun notifyObservers(value: Boolean) {

        for(o in observers){
            o.update(value)
        }

    }

    fun registerNetWorkConnection(){

        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback)

    }

    fun unRegisterNetWorkConnection(){

        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)

    }


}