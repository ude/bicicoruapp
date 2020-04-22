package com.udepardo.bicicoru.data.source.remote

import com.udepardo.bicicoru.data.model.remote.bikes.CityBikeWrapper
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by fernando.ude on 17/01/2018.
 */
interface CityBikesInterfaceApi {

    @GET("/v2/networks/bicicorunha")
    fun requestBikeStations(): Call<CityBikeWrapper>
}