package com.udepardo.bicicoru.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import android.content.SharedPreferences
import android.util.Log
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.model.db.UserTrackKt
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource


@Dao
interface ProfileDao : ProfileLocalDatasource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertUserProfile(personalData: UserProfileKt)

    @Query("SELECT * FROM personalData LIMIT 1")
    override fun getUserProfile(): UserProfileKt

    @Query("SELECT * FROM graphcoords ORDER BY id ASC")
    override fun getUserStats(): List<GraphCoord>

    @Query("SELECT * FROM tracks WHERE (beginTime >=  :beginDate AND endTime <= :endDate) ORDER BY beginTime ASC")
    override fun getUserTracks(beginDate: Long, endDate: Long): List<UserTrackKt>


    @Query("SELECT * FROM tracks")
    fun getUserTracks(): List<UserTrackKt>

    @Query("DELETE from personalData")
    override fun deleteUserProfile()

    @Query("DELETE from graphcoords")
    override fun deleteStats()

    @Query("DELETE from tracks")
    override fun deleteTracks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertUserStats(coords: List<GraphCoord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertUserTracks(tracks: List<UserTrackKt>)

}



