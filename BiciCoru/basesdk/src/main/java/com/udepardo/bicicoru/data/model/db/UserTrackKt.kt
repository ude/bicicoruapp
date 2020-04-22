package com.udepardo.bicicoru.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by ude on 1/7/17.
 */
@Entity(tableName = "tracks")
data class UserTrackKt (
        @PrimaryKey val beginTime: Long,
        @ColumnInfo val endTime: Long,
        @ColumnInfo val beginStation: String,
        @ColumnInfo val endStation: String)