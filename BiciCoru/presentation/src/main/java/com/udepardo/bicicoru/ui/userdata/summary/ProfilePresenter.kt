package com.udepardo.bicicoru.ui.userdata.summary

import androidx.lifecycle.LifecycleOwner
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.user.UserProfileLiveData
import com.udepardo.bicicoru.domain.interactor.user.UserStatsLiveData
import com.udepardo.bicicoru.domain.interactor.user.UserTracksLiveData
import com.udepardo.bicicoru.domain.model.TrackViewModel
import com.udepardo.bicicoru.domain.model.TracksInput
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

class MainPresenter(val view: MainViewTranslator) : KoinComponent {
    val repository: ProfileDatasource by inject()
    val profileDao: ProfileLocalDatasource by inject()

    private val calendarTo = Calendar.getInstance()
    private val calendarFrom = Calendar.getInstance()

    private val profileUseCaseKt: UserProfileLiveData by lazy {
        UserProfileLiveData(
            repository,
            profileDao
        )
    }
    private val userStatsUseCaseKt: UserStatsLiveData by lazy {
        UserStatsLiveData(
            repository,
            profileDao
        )
    }
    private val userTracksUseCase: UserTracksLiveData by lazy {
        UserTracksLiveData(
            repository,
            profileDao
        )
    }

    init {
        profileUseCaseKt.observe(view.getLifecycleOwner(), androidx.lifecycle.Observer { it?.let { profile-> loadProfile(profile) } })
        userStatsUseCaseKt.observe(view.getLifecycleOwner(), androidx.lifecycle.Observer { it?.let { stats -> loadGraph(stats) } })
        userTracksUseCase.observe(view.getLifecycleOwner(), androidx.lifecycle.Observer { it?.let { tracks -> loadRecentTracks(tracks) } })

    }

    fun start() {
        view.showSpinner()
        loadData()
    }

    private fun loadData() {
        profileUseCaseKt.execute {}
        userStatsUseCaseKt.execute {}
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0)
        calendarFrom.set(Calendar.MINUTE, 0)
        calendarFrom.set(Calendar.SECOND, 0)
        calendarFrom.add(Calendar.YEAR, -1)

        calendarTo.set(Calendar.HOUR_OF_DAY, 23)
        calendarTo.set(Calendar.MINUTE, 59)
        calendarTo.set(Calendar.SECOND, 59)


        userTracksUseCase.execute(TracksInput(calendarFrom.timeInMillis, calendarTo.timeInMillis, 3)){}
    }


    private fun loadProfile(data: BCResult<UserProfileKt>) {
        view.hideSpinner()
        data.data?.let {
            view.showProfile(it)
        }
    }

    private fun loadGraph(data: BCResult<List<GraphCoord>>) {
        view.hideSpinner()
        data.data?.let {
            view.showStats(it)
        }
    }

    private fun loadRecentTracks(data: BCResult<List<TrackViewModel>>) {
        view.hideSpinner()
        data.data?.let {
            view.showTracks(it)
        }

    }
}


interface MainViewTranslator {
    fun showSpinner()
    fun hideSpinner()
    fun showProfile(userProfile: UserProfileKt)
    fun showStats(userStats: List<GraphCoord>)
    fun showTracks(userTracks: List<TrackViewModel>)
    fun getLifecycleOwner(): LifecycleOwner
}


