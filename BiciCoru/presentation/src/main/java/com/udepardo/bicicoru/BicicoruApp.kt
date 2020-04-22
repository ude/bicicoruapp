package com.udepardo.bicicoru

import android.app.Application
import com.udepardo.bicicoru.data.injection.DBModule
import com.udepardo.bicicoru.data.injection.NetModule
import com.udepardo.bicicoru.data.injection.NewDomainModule
import org.koin.android.ext.android.startKoin

class BicicoruApp : Application() {

    companion object {
        lateinit var instance: BicicoruApp
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin(this, listOf(DBModule(this).dbModule, NetModule().networkModule, NewDomainModule(this).domainModule))
    }
}
