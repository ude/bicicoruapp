package com.udepardo.bicicoru.data.repositories

import com.udepardo.bicicoru.data.model.db.StationInfoModel


interface BikesDatasource {
    fun getBikeStations(): List<StationInfoModel>?
}


interface BikesLocalDatasource: BikesDatasource {
    fun deleteStations()
    fun insertStations(stations: List<StationInfoModel>)
}