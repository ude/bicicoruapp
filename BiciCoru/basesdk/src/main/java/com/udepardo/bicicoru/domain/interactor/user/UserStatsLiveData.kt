package com.udepardo.bicicoru.domain.interactor.user

import androidx.lifecycle.LiveData
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.KtInteractor
import java.io.IOException
import javax.security.auth.login.LoginException


class UserStatsLiveData(val remote: ProfileDatasource, val local: ProfileLocalDatasource) :
        KtInteractor<Unit, BCResult<List<GraphCoord>>>, LiveData<BCResult<List<GraphCoord>>>() {


    override fun syncCall(params: Unit?): BCResult<List<GraphCoord>> {
        postValue(BCResult(local.getUserStats()))
        try {
            remote.getUserStats().apply {
                local.insertUserStats(this)
                postValue(BCResult(this))
            }
        }catch (exception: IOException){
            postValue(BCResult(exception =  exception))
        }catch (exception: LoginException){
            postValue(BCResult(exception =  exception))
        }
        return BCResult(local.getUserStats())

    }
}