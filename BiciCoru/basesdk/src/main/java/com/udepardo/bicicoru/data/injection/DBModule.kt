package com.udepardo.bicicoru.data.injection

import androidx.room.Room
import android.content.Context
import android.preference.PreferenceManager
import com.udepardo.bicicoru.data.repositories.BikesLocalDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.data.source.local.*
import org.koin.dsl.module.module


/**
 * Created by fernando.ude on 10/04/2018.
 */


class DBModule(val context: Context) {

    val dbModule = module {
        val db = Room.databaseBuilder(context, BicicoruDB::class.java, "bicicoruDB").build()

        single {
            db.bikesDao() as BikesLocalDatasource
        }

        single {
            db.profileDao() as ProfileLocalDatasource
        }

        single {
            PreferenceManager.getDefaultSharedPreferences(context)
        }

    }

}



