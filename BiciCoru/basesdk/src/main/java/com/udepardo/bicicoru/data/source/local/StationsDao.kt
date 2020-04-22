package com.udepardo.bicicoru.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesLocalDatasource

/**
 * Created by fernando.ude on 10/04/2018.
 */


@Dao
interface StationsDao : BikesLocalDatasource {

    @Query("SELECT * FROM stations")
    override fun getBikeStations(): List<StationInfoModel>

    @Query("DELETE from stations")
    override fun deleteStations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertStations(stations: List<StationInfoModel>)


}
