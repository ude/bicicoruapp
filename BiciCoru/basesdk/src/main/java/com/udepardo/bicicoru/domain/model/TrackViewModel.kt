package com.udepardo.bicicoru.domain.model


/**
 * Created by ude on 1/7/17.
 */
data class TrackViewModel(
        val id: Long,
        val beginTime: String,
        val duration: String,
        val beginStation: String,
        val endStation: String
)