package com.udepardo.bicicoru.data.injection

import android.content.Context
import android.preference.PreferenceManager
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.local.BikesLocalRepository
import com.udepardo.bicicoru.data.repositories.remote.bikes.BiciCoruRemoteRepository
import com.udepardo.bicicoru.data.repositories.remote.bikes.CityBikesRepository
import com.udepardo.bicicoru.data.repositories.remote.userdata.UserDataRemote
import com.udepardo.bicicoru.data.source.local.StationsDao
import com.udepardo.bicicoru.data.source.remote.BiciCoruInterfaceApi
import com.udepardo.bicicoru.data.source.remote.CityBikesInterfaceApi
import okhttp3.OkHttpClient
import org.koin.dsl.module.module

class NewDomainModule(val context: Context){
    val domainModule = module {
        single { provideBicicoruRemoteRepository(get()) }
        single<BikesDatasource> ("BCRepo") { provideBicicoruRemoteRepository(get()) }
        single<BikesDatasource> ("CBRepo"){ provideCityBikesRemoteRepository(get())}
        single { provideStationLocalRepository(get())}
        single { provideUserDataRepository(get())}
    }


    fun provideBicicoruRemoteRepository(bikeInterfaceApi: BiciCoruInterfaceApi): BiciCoruRemoteRepository {
        return BiciCoruRemoteRepository(bikeInterfaceApi)
    }

    private fun provideCityBikesRemoteRepository(bikeInterfaceApi: CityBikesInterfaceApi): CityBikesRepository {
        return CityBikesRepository(bikeInterfaceApi)
    }

    private fun provideStationLocalRepository(stationsDao: StationsDao): BikesLocalRepository {
        return BikesLocalRepository(stationsDao)
    }

    private fun provideUserDataRepository(client: OkHttpClient): ProfileDatasource {
        return UserDataRemote(PreferenceManager.getDefaultSharedPreferences(context), client)
    }
}