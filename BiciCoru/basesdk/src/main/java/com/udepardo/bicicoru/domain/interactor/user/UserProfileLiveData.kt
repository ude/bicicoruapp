package com.udepardo.bicicoru.domain.interactor.user

import androidx.lifecycle.LiveData
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.KtInteractor
import java.io.IOException
import javax.security.auth.login.LoginException


class UserProfileLiveData(val remote: ProfileDatasource,
                          val local: ProfileLocalDatasource) : KtInteractor<Unit, BCResult<UserProfileKt>>, LiveData<BCResult<UserProfileKt>>() {
    override fun syncCall(params: Unit?): BCResult<UserProfileKt> {
        try {
            postValue(BCResult(local.getUserProfile()))
            remote.getUserProfile().apply {
                local.insertUserProfile(this)
                postValue(BCResult(this))
            }
        } catch (exception: IOException) {
            postValue(BCResult(exception = exception))

        } catch (loginException: LoginException) {
            postValue(BCResult(exception = loginException))
        }
        return BCResult(local.getUserProfile())

    }

}