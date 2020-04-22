package com.udepardo.bicicoru.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.model.db.UserTrackKt
import org.json.JSONException
import java.io.IOException
import javax.security.auth.login.LoginException


interface ProfileDatasource {

    fun getUserProfile(): UserProfileKt
    fun getUserStats(): List<GraphCoord>

    @Throws(IOException::class, JSONException::class, LoginException::class)
    fun getUserTracks(beginDate: Long, endDate: Long): List<UserTrackKt>
}


interface ProfileLocalDatasource : ProfileDatasource {
    fun insertUserProfile(personalData: UserProfileKt)
    fun insertUserStats(coords: List<GraphCoord>)
    fun insertUserTracks(tracks: List<UserTrackKt>)

    fun deleteUserProfile()
    fun deleteStats()
    fun deleteTracks()

}