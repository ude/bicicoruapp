package com.udepardo.bicicoru.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.udepardo.bicicoru.data.model.db.StationInfoModel

/**
 * Created by fernando.ude on 16/02/2018.
 */


data class StationsViewModel(val stations: List<StationViewModel>) {
    object Mapper {
        fun from(input: List<StationInfoModel>): StationsViewModel {
            return StationsViewModel(input.map { StationViewModel.Mapper.from(it) })
        }
    }

}

data class StationViewModel(
    val id: Int,
    val name: String,
    val emptySlots: Int,
    val bikes: Int,
    val pos: LatLng,
    val isOnline: Boolean
) : ClusterItem {
    override fun getSnippet(): String {
        return bikes.toString()
    }

    override fun getTitle(): String {
        return name
    }

    override fun getPosition(): LatLng {
        return pos
    }

    object Mapper {
        fun from(input: StationInfoModel): StationViewModel {
            return StationViewModel(input.id, input.name, input.emptySlots, input.bikes, LatLng(input.latitude, input.longitude), input.isOnline)
        }
    }
}