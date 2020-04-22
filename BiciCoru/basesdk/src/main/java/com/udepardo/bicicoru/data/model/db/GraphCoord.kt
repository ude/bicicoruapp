package com.udepardo.bicicoru.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "graphcoords")
data class GraphCoord(@PrimaryKey val id: Int, @ColumnInfo val x: String, @ColumnInfo val y: Int)

