package com.example.simplemediaapi.utils

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import androidx.lifecycle.LiveData
import java.lang.IllegalStateException

/**
 * Данный класс реализует LiveData<Boolean>, поэтому может быть под наблюдением (observable).
 * @param context контекст следящей активити
 */
class NetworkConnection(private val context: Context): LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val validNetworks: MutableSet<Network> = HashSet()

    /**
     * Вызывается при изменении количества наблюдателей с 0 до 1.
     */
    override fun onActive() {
        super.onActive()
        checkValidNetworks()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback()) // TODO do as in LOLIPOP
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                lolipopNetworkRequest()
            }
            else -> {
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }

        }
    }

    /**
     * Вызывается при изменении количества наблюдателей с 1 до 0.
     */
    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lolipopNetworkRequest() {
        val requestBuilder = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectivityManager.registerNetworkCallback(
            requestBuilder.build(),
            connectivityManagerCallback()
        )
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = object: ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
//                    postValue(false)
                    validNetworks.remove(network)
                    checkValidNetworks()
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
                    if (hasInternetCapability == true)
                        validNetworks.add(network)
                        checkValidNetworks()

//                    else
//                        postValue(false)
                }
            }
            return networkCallback

        } else {
            throw IllegalStateException("Creation NetworkCallback on API level less than 21")
        }
    }

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    private var networkReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }

    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }
}