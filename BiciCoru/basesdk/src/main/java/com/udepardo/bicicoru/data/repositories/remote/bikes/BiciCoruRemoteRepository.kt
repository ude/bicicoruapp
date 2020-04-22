package com.udepardo.bicicoru.data.repositories.remote.bikes

import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.source.remote.BiciCoruInterfaceApi
import java.io.IOException

/**
 * Created by fernando.ude on 18/01/2018.
 */


class BiciCoruRemoteRepository(private val bikeInterfaceApi: BiciCoruInterfaceApi) : BikesDatasource {

    @Throws(IOException::class)
    override fun getBikeStations(): List<StationInfoModel>? {
        val call = bikeInterfaceApi.requestBikeStations()
        val wrapper = call.execute().body()


        val result = wrapper?.additionalInfo


        return result?.let { list ->
            list.map { StationInfoModel.ModelMapper.from(it) }
        }
    }
}