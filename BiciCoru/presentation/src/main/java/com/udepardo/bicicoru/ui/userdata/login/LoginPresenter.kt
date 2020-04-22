package com.udepardo.bicicoru.ui.userdata.login

import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.user.UserProfileUseCaseKt
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class LoginPresenter(val view: LoginViewTranslator) : KoinComponent {

    val remoteDataSource: ProfileDatasource by inject()
    val localDatasource: ProfileLocalDatasource by inject()

    private val userProfileUseCaseKt: UserProfileUseCaseKt

    init {
        userProfileUseCaseKt = UserProfileUseCaseKt(remoteDataSource, localDatasource)
    }

    fun loginPressed(login: String, password: String) {
        view.showLoader()

        if (login.isBlank() || password.isBlank()) {
            view.hideLoader()
            view.showToast("Asegurate de que los datos son correctos")
            return
        }
        userProfileUseCaseKt.execute(callback = {
            view.hideLoader()
            it.data?.let { userProfile ->
                login(userProfile)
            } ?: showError(it.exception!!)
        })
    }


    private fun login(userProfileKt: UserProfileKt) {
        view.hideError()
        view.closeSuccess(userProfileKt)
    }

    private fun showError(exception: Exception) {

        view.showError("Error status ${exception.localizedMessage}")
    }
}

interface LoginViewTranslator {
    fun showLoader()
    fun hideLoader()
    fun showError(message: String)
    fun hideError()
    fun closeSuccess(userProfile: UserProfileKt)
    fun showToast(message: String)

}