package com.snc.farmaccount

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.util.DisplayMetrics
import android.widget.Toast
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.snc.farmaccount.helper.dpToPx


class CheckInternet: BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, internet: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetworkInfo
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val content = layout.findViewById(R.id.internet_content) as TextView
        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM, 0, 50.dpToPx())
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout

        //take action when network is closed
        if (network == null) {
            content.text = ApplicationContext.applicationContext().getString(R.string.no_internet)
            toast.show()
        }

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
                content.text = ApplicationContext.applicationContext().getString(R.string.internet_gained)
                toast.show()
            }

            override fun onLost(network: Network?) {
                //take action when network connection is lost
                content.text = ApplicationContext.applicationContext().getString(R.string.internet_lost)
                toast.show()
            }
        })
    }

}