package com.udepardo.bicicoru.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.model.db.UserTrackKt

/**
 * Created by fernando.ude on 10/04/2018.
 */

@Database(entities = [StationInfoModel::class, UserProfileKt::class, GraphCoord::class, UserTrackKt::class], version = 1, exportSchema = false)

abstract class BicicoruDB : RoomDatabase() {
    abstract fun bikesDao(): StationsDao
    abstract fun profileDao() : ProfileDao
}