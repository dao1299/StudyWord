package com.example.kltnstudyword

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context):LiveData<Boolean>() {
    private var connect: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipop(){
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connect.registerNetworkCallback(
            requestBuilder.build(),
            connectCallback()
        )
    }

    override fun onActive() {
        super.onActive()
        updateCon()
        when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.N ->{
                connect.registerDefaultNetworkCallback(connectCallback())
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP->{
                lollipop()
            }
            else ->{
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            connect.unregisterNetworkCallback(connectCallback())
        }else{
            context.unregisterReceiver(networkReceiver)
        }
    }

    private fun connectCallback(): ConnectivityManager.NetworkCallback{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }
            }
            return networkCallback
        }
        else{
            throw IllegalAccessError("Error")
        }
    }

    private val networkReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            updateCon()
        }
    }

    private fun updateCon(){
        val activeNetwork: NetworkInfo?= connect.activeNetworkInfo
        postValue(activeNetwork?.isConnected==true)
    }
}