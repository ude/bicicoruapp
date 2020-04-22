package com.udepardo.bicicoru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udepardo.bicicoru.ui.maps.MapsActivity


class SplashActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsActivity.startActivity(this)
    }
}