package com.udepardo.bicicoru.domain.interactor.user

import android.content.SharedPreferences
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.KtInteractor

class UserLogoutUseCase(private val preferences: SharedPreferences,
                        val local: ProfileLocalDatasource
) : KtInteractor<Unit, BCResult<Unit>> {

    override fun syncCall(params: Unit?): BCResult<Unit> {
        local.deleteStats()
        local.deleteTracks()
        local.deleteUserProfile()
        preferences.edit().putBoolean("loggedin", false).apply()
        return BCResult(data = Unit)
    }
}