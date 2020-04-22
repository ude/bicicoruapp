package com.udepardo.bicicoru.data.model.remote.bikes

import androidx.annotation.Keep

/**
 * Created by fernando.ude on 17/01/2018.
 */

@Keep
data class CityBikeWrapper(val network: CityBikeNetwork)

@Keep
data class CityBikeExtra(val slots: Int, val status: String, val uid: Int)

@Keep
data class CityBikeNetwork(
    val company: List<String>,
    val href: String,
    val id: String,
    val location: CityBikeNetworkLocation,
    val name: String,
    val stations: List<CityBikeStation>
)

@Keep
data class CityBikeNetworkLocation(val city: String, val country: String, val latitude: Double, val longitude: Double)

@Keep
data class CityBikeStation(
    val empty_slots: Int,
    val extra: CityBikeExtra,
    val free_bikes: Int,
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val timestamp: String
)



