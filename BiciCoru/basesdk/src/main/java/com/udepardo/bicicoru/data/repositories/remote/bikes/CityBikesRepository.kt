package com.udepardo.bicicoru.data.repositories.remote.bikes

import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.source.remote.CityBikesInterfaceApi
import java.io.IOException

/**
 * Created by fernando.ude on 17/01/2018.
 */


class CityBikesRepository(private val bikeInterfaceApi: CityBikesInterfaceApi) : BikesDatasource {

    @Throws(IOException::class)
    override fun getBikeStations(): List<StationInfoModel> {

        val call = bikeInterfaceApi.requestBikeStations()

        val list = call.execute().body()?.network?.stations

        return list?.let {
            it.map { StationInfoModel.ModelMapper.from(it) }
        } ?: listOf()

    }
}