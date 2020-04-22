package com.udepardo.bicicoru.domain.interactor

import androidx.lifecycle.LiveData
import com.udepardo.bicicoru.data.model.db.StationInfoModel
import com.udepardo.bicicoru.data.repositories.BikesDatasource
import com.udepardo.bicicoru.data.repositories.BikesLocalDatasource
import java.io.IOException

/**
 * Created by fernando.ude on 10/04/2018.
 */

class GetBikesLiveDataUseCase(private val remote: BikesDatasource,
                              private val local : BikesLocalDatasource) :
        KtInteractor<Unit, BCResult<List<StationInfoModel>>>, LiveData<BCResult<List<StationInfoModel>>>() {


    override fun syncCall(params: Unit?): BCResult<List<StationInfoModel>> {
        try {
            postValue(BCResult(local.getBikeStations()))
            remote.getBikeStations()?.let {
                local.insertStations(it)
                postValue(BCResult(local.getBikeStations()))
            }

        }catch (ex: IOException){
            postValue(BCResult(exception = ex))
        }
        return BCResult(local.getBikeStations())

    }

//    override fun start(params: Unit?) {
//        runOnExecutor {
//            val wrapper: DataWrapper<List<StationInfoModel>> = DataWrapper()
//            wrapper.status = DataWrapper.Status.SUCCESS
//            wrapper.data = bikesLocalDatasource.getBikeStations()
//            postValue(wrapper)
//            fetchData(wrapper)
//        }
//    }
//
//
//    private fun fetchData(wrapper: DataWrapper<List<StationInfoModel>>) {
//        runOnExecutor {
//            try {
//                with(repository.getBikeStations()) {
//                    if (!this.isEmpty()) {
//                        wrapper.data = this
//                        bikesLocalDatasource.insertStations(wrapper.data)
//                        wrapper.status = DataWrapper.Status.SUCCESS
//                        postValue(wrapper)
//                    }
//                }
//            } catch (ex: IOException) {
//                wrapper.setError(ex)
//            } catch (ex: IllegalArgumentException) {
//                wrapper.setError(ex)
//            }
//            Handler().postDelayed({ fetchData(wrapper) }, 3 * 60 * 1000)
//        }
//    }
}