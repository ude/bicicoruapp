package com.udepardo.bicicoru.ui.userdata

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.udepardo.bicicoru.BicicoruApp
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.user.UserLogoutUseCase
import com.udepardo.bicicoru.domain.interactor.user.UserProfileUseCaseKt
import com.udepardo.bicicoru.ui.userdata.summary.ProfileFragment
import kotlinx.android.synthetic.main.settings_fragment.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class SettingsFragment : androidx.fragment.app.Fragment(), SettingsViewTranslator {

    private val settingsPresenter = SettingsPresenter(this)
    private var clearbackStack: (() -> Unit)? = null

    companion object {
        fun start(logout: () -> Unit) = SettingsFragment().apply {
            setRemoveFragmetnsListener(logout)
        }
    }

    fun start(launchLogin: (androidx.fragment.app.Fragment) -> Unit, removeFragments: () -> Unit): ProfileFragment = ProfileFragment().apply {
        setAddFragmentListener(launchLogin)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.settings_fragment, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        privacy.setOnClickListener {
            val url = "http://bicicoru.herokuapp.com/license"
            val i = Intent(Intent.ACTION_VIEW).also { intent -> intent.data = Uri.parse(url) }
            startActivity(i)
        }

        settingsButton.setOnClickListener {
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
        }
        logout.setOnClickListener {
            settingsPresenter.logout()
        }

    }

    fun setRemoveFragmetnsListener(listener: () -> Unit) {
        clearbackStack = listener
    }

    override fun performLogout() {
        clearbackStack?.invoke()
    }

}

interface SettingsViewTranslator {
    fun performLogout()
}


class SettingsPresenter(val view: SettingsViewTranslator) : KoinComponent {

    val localDatasource: ProfileLocalDatasource by inject()


    val prefsremces: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(BicicoruApp.instance)
    }

    private val userLogoutUseCase: UserLogoutUseCase by lazy { UserLogoutUseCase(prefsremces, localDatasource) }

    fun logout() {
        userLogoutUseCase.execute {
            view.performLogout()
        }
//        PreferenceManager.getDefaultSharedPreferences(BicicoruApp.instance)
//        localDatasource.apply {
//            deleteStats()
//            deleteTracks()
//            deleteUserProfile()
//        }


    }
}

