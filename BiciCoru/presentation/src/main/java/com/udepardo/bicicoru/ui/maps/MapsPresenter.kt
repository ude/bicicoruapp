package com.udepardo.bicicoru.ui.maps

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import android.location.Location
import com.udepardo.bicicoru.data.model.StationViewModel
import com.udepardo.bicicoru.data.model.StationsViewModel
import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.repositories.BikesLocalDatasource
import com.udepardo.bicicoru.domain.interactor.GetBikesLiveDataUseCase
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class MapsPresenter(val view: MapsViewTranslator) : KoinComponent {
    private val defaultLocation = Location("").apply {
        latitude = 43.3623
        longitude = -8.4115
    }

    val repository: BikesDatasource by inject("BCRepo")
    val bikesDatasource: BikesLocalDatasource by inject()

    val liveData: GetBikesLiveDataUseCase

    init {
        liveData = GetBikesLiveDataUseCase(repository, bikesDatasource)
        liveData.execute {  }
    }


    fun mapLoaded() {
        view.moveCameraTo(defaultLocation, 14f)
        view.checkPermissions()
        liveData.observe(view.getLifeCycleOwner(), Observer { wrapper ->
            if (wrapper?.data != null) {
                val data = StationsViewModel.Mapper.from(wrapper.data as List<StationInfoModel>)
                view.hideList()
                view.updateClusterManager(data.stations)
                view.updateList(data.stations)

            }
        })
    }

    fun zoomedOut() {
        view.hideList()
    }

    fun permissionsGranted() {
        view.getFirstLocation()
        view.subscribeToLocationUptades()
    }

    fun permissionsDenied() {

    }


    fun firstLocationAcquired(location: Location) {
        view.moveCameraTo(location, 15f)
    }

    fun locationAcquired(location: Location) {
        view.updateAdapterLocation(location)
    }
}


interface MapsViewTranslator {
    fun showList()
    fun hideList()
    fun checkPermissions()
    fun getLifeCycleOwner(): LifecycleOwner
    fun onListItemClicked(stationViewModel: StationViewModel)
    fun onNavigateClicked(stationViewModel: StationViewModel)
    fun updateClusterManager(items: List<StationViewModel>)
    fun updateList(items: List<StationViewModel>)
    fun updateAdapterLocation(location: Location)
    fun moveCameraTo(location: Location, zoom: Float?)
    fun subscribeToLocationUptades()
    fun unsubscribeToLocationUpdates()
    fun getFirstLocation()
}