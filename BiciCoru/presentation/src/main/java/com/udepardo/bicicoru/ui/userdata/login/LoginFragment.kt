package com.udepardo.bicicoru.ui.userdata.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.udepardo.bicicoru.BicicoruApp
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.extensions.hideKeyboard
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.error


class LoginFragment : androidx.fragment.app.Fragment(), LoginViewTranslator {

    private val loginPresenter = LoginPresenter(this)

    private var profileCallback: (() -> Unit)? = null

    companion object {
        fun start(listener: () -> Unit): LoginFragment {
            return LoginFragment().apply {
                setCallback(listener)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.activity_login, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            it.hideKeyboard()
            hideError()
            PreferenceManager.getDefaultSharedPreferences(BicicoruApp.instance).edit().apply {
                putString("login", login.text.toString())
                putString("password", password.text.toString())

            }.apply()
            loginPresenter.loginPressed(login.text.toString(), password.text.toString())

        }
        privacy.setOnClickListener {
            val url = "http://bicicoru.herokuapp.com/license"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


        cancelButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

    }

    fun setCallback(callback: () -> Unit) {
        profileCallback = callback
    }

    override fun showLoader() {
        loader?.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        loader?.visibility = View.GONE
    }

    override fun showError(message: String) {
        error.text = message
        error.visibility = View.VISIBLE
        Handler().postDelayed(
            {
                activity?.let {
                    login.text.clear()
                    password.text.clear()
                    login.requestFocus()
                    hideError()
                }

            }, 3500
        )
    }

    override fun hideError() {
        error.visibility = View.INVISIBLE
    }

    override fun closeSuccess(userProfile: UserProfileKt) {
        profileCallback?.invoke()
    }

    override fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }

    }
}