package com.udepardo.bicicoru.data.source.remote

import com.udepardo.bicicoru.data.model.remote.bikes.BicicoruWrapper
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by fernando.ude on 26/01/2018.
 */
interface BiciCoruInterfaceApi {

    @GET("/stations")
    fun requestBikeStations(): Call<BicicoruWrapper>
}