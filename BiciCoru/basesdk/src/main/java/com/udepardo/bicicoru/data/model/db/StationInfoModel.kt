package com.udepardo.bicicoru.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udepardo.bicicoru.data.model.remote.bikes.BicicorunaStation
import com.udepardo.bicicoru.data.model.remote.bikes.CityBikeStation

/**
 * Created by fernando.ude on 18/01/2018.
 */

@Entity(tableName = "stations")
data class StationInfoModel(
        @PrimaryKey val id: Int,
        @ColumnInfo val name: String,
        @ColumnInfo val emptySlots: Int,
        @ColumnInfo val bikes: Int,
        @ColumnInfo val isOnline: Boolean,
        @ColumnInfo val latitude: Double,
        @ColumnInfo val longitude: Double
) {
    object ModelMapper {
        fun from(input: CityBikeStation): StationInfoModel {
            return StationInfoModel(
                input.extra.uid,
                input.name,
                input.empty_slots,
                input.free_bikes,
                input.extra.status == "online",
                input.latitude,
                input.longitude
            )
        }

        fun from(input: BicicorunaStation): StationInfoModel {
            return (StationInfoModel(
                input.id,
                input.name,
                input.free,
                input.occupied,
                input.online == "true",
                input.lat,
                input.lon
            ))
        }

    }


}
