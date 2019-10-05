package com.snc.farmaccount

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import android.widget.Toast
import com.snc.farmaccount.helper.SOPHIE

class CheckInternet: BroadcastReceiver() {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, internet: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetworkInfo
        Log.i(SOPHIE, "network:$network")

        if (network == null) {
            Toast.makeText(context,"No internet", Toast.LENGTH_SHORT).show()
        }

        connectivityManager?.let {
            it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    //take action when network connection is gained
                    Toast.makeText(context,"network connection is gained", Toast.LENGTH_SHORT).show()
                }
                override fun onLost(network: Network?) {
                    //take action when network connection is lost
                    Toast.makeText(context,"network connection is lost", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}