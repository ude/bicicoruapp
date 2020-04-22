package com.udepardo.bicicoru.domain.interactor.user

import android.content.SharedPreferences
import android.util.Log
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.KtInteractor
import java.io.IOException
import javax.security.auth.login.LoginException

/**
 * Created by fernando.ude on 19/01/2018.
 */


class UserProfileUseCaseKt(
        val remote: ProfileDatasource,
        val local: ProfileLocalDatasource
) : KtInteractor<Unit, BCResult<UserProfileKt>> {
    override fun syncCall(params: Unit?): BCResult<UserProfileKt> {
        try {
            remote.getUserProfile().apply {
                local.insertUserProfile(this)
                return BCResult(this)

            }
        } catch (e: IOException) {
            return BCResult(exception = e)
        } catch (loginException: LoginException) {
            return BCResult(exception = loginException)
        } catch (exception: Exception) {
            return BCResult(exception = exception)
        }
    }
}