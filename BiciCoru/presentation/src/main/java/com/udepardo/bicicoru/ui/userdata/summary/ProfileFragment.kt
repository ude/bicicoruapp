package com.udepardo.bicicoru.ui.userdata.summary

import androidx.lifecycle.LifecycleOwner
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.domain.model.TrackViewModel
import com.udepardo.bicicoru.extensions.gone
import com.udepardo.bicicoru.extensions.visible
import com.udepardo.bicicoru.ui.userdata.SettingsFragment
import kotlinx.android.synthetic.main.profile_fragment.*


class ProfileFragment : androidx.fragment.app.Fragment(), MainViewTranslator {


    private lateinit var presenter: MainPresenter
    private val adapter = UserTrackAdapter()

    private var addFragmentListener: ((androidx.fragment.app.Fragment) -> Unit)? = null
    private var clearbackStack: (() -> Unit)? = null

    companion object {
        fun start(launchLogin: (androidx.fragment.app.Fragment) -> Unit, removeFragments: () -> Unit): ProfileFragment = ProfileFragment().apply {
            setAddFragmentListener(launchLogin)
            setRemoveFragmetnsListener(removeFragments)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, null)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            recentTracksList.context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        )
        recentTracksList.addItemDecoration(dividerItemDecoration)
        recentTracksList.adapter = adapter
        settingsButton.setOnClickListener { showSettings()  }

        presenter.start()
    }

    override fun showSpinner() {
        spinner.visible()
    }

    override fun hideSpinner() {
        spinner.gone()
    }


    fun setAddFragmentListener(listener: (androidx.fragment.app.Fragment) -> Unit) {
        addFragmentListener = listener
    }

    fun setRemoveFragmetnsListener(listener: () -> Unit){
        clearbackStack = listener
    }


    override fun showProfile(userProfile: UserProfileKt) {
        profileName.text = "${userProfile.name} ${userProfile.surname} ${userProfile.lastName}"
        profileCardNumber.text = "${userProfile.cardNumber}"
    }

    override fun showStats(userStats: List<GraphCoord>) {
        if (userStats.isNotEmpty()){
            chartStatGraph.setData(userStats)
        }
    }


    override fun showTracks(userTracks: List<TrackViewModel>) {
        adapter.updateModel(userTracks)
    }

    override fun getLifecycleOwner(): LifecycleOwner = this

    private fun showSettings(){
        addFragmentListener?.invoke(SettingsFragment.start { clearbackStack?.invoke() })
    }

}