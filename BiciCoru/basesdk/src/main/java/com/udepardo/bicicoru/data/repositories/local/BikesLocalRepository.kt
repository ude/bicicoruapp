package com.udepardo.bicicoru.data.repositories.local

import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.source.local.StationsDao
import java.io.IOException

/**
 * Created by fernando.ude on 10/04/2018.
 */

class BikesLocalRepository (private val stationsDao: StationsDao) : BikesDatasource {

    @Throws(IOException::class)
    override fun getBikeStations(): List<StationInfoModel> {
        return stationsDao.getBikeStations()
    }
}