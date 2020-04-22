package com.udepardo.bicicoru.domain.interactor.user

import androidx.lifecycle.LiveData
import com.udepardo.bicicoru.data.model.db.UserTrackKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.ProfileLocalDatasource
import com.udepardo.bicicoru.domain.interactor.BCResult
import com.udepardo.bicicoru.domain.interactor.KtInteractor
import com.udepardo.bicicoru.domain.model.TrackViewModel
import com.udepardo.bicicoru.domain.model.TracksInput
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import org.json.JSONException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UserTracksLiveData(val remote: ProfileDatasource, val local: ProfileLocalDatasource) :
        KtInteractor<TracksInput, BCResult<List<TrackViewModel>>>, LiveData<BCResult<List<TrackViewModel>>>() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    //TODO: Esto tendría que venir de fuera
    val formatter = PeriodFormatterBuilder()
            .appendYears().appendSuffix(" año, ", " años, ")
            .appendMonths().appendSuffix(" mes, ", " meses, ")
            .appendWeeks().appendSuffix(" semana, ", " semanas, ")
            .appendDays().appendSuffix(" dia, ", " dias, ")
            .appendHours().appendSuffix(":")
            .minimumPrintedDigits(2)
            .appendMinutes().appendSuffix(":")
            .appendSeconds()
            .toFormatter()


    fun mapList(list: List<UserTrackKt>): List<TrackViewModel> {
        return list.map {
            TrackViewModel(
                    it.beginTime,
                    dateFormatter.format(it.beginTime),
                    formatter.print(Period(DateTime(it.beginTime), DateTime(it.endTime))),
                    it.beginStation,
                    it.endStation)
        }.sortedBy {
            it.beginTime
        }
    }

    override fun syncCall(params: TracksInput?): BCResult<List<TrackViewModel>> {
        if (params == null) return BCResult(exception = java.lang.IllegalArgumentException("Wrong Arguments"))
        return try {
            local.getUserTracks(params.beginDate, params.endDate).apply {
                postValue(BCResult(mapList(this.sortedBy { it.beginTime })))
            }

            remote.getUserTracks(params.beginDate, params.endDate).apply {
                local.insertUserTracks(this)
                postValue(BCResult(mapList(local.getUserTracks(params.beginDate, params.endDate).sortedBy { it.beginTime })))
            }
            BCResult(mapList(local.getUserTracks(params.beginDate, params.endDate)))
        } catch (e: IOException) {
            BCResult(exception = e)
        } catch (e: JSONException) {
            BCResult(exception = e)
        }


    }
}