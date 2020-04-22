package com.udepardo.bicicoru.domain.interactor

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import java.util.*

@SuppressLint("MissingPermission")
class LocationInteractorKt(context: Context) : LiveData<Location>() {


    companion object {
        const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000L
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        @Volatile
        var instance: LocationInteractorKt? = null

        fun getInstance(context: Context): LocationInteractorKt = instance
            ?: synchronized(this) {
            instance
                ?: LocationInteractorKt(context).also { instance = it }
        }
    }

    var mCurrentLocation: Location? = null
    var lastUpdateTime: Long = 0L

    private val mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)

    private val mLocationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)



                result?.run {  }

                result?.apply {
                    Log.d("LocationInteractor", "Location received acc: {${mCurrentLocation?.accuracy}}")
                    mCurrentLocation = this.lastLocation
                    lastUpdateTime = Date().time
                    value = mCurrentLocation;
                }

            }
        }
    }


    private val mLocationRequest: LocationRequest by lazy {
        LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val mLocationSettingsRequest: LocationSettingsRequest by lazy {
        LocationSettingsRequest.Builder().apply {
            addLocationRequest(mLocationRequest);
        }.build()
    }


    init {
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            value = it
        }
    }

    private fun startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener {
            try {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            } catch (ex: SecurityException) {
                Log.e("LocationInteractor", ex.localizedMessage)
            }
        }.addOnFailureListener {
            Log.e("LocationInteractor", it.localizedMessage)

        }
    }

    private fun stopLocationUpdates() = mFusedLocationClient.removeLocationUpdates(mLocationCallback)

    override fun onActive() = startLocationUpdates()

    override fun onInactive() {
        stopLocationUpdates()
    }

}