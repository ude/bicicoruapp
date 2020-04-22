package com.udepardo.bicicoru.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "personalData")
data class UserProfileKt(@PrimaryKey val cardNumber: String,
                         @ColumnInfo val dni: String,
                         @ColumnInfo val name : String,
                         @ColumnInfo val surname : String,
                         @ColumnInfo val lastName : String,
                         @ColumnInfo val birthDate : String,
                         @ColumnInfo val address : String,
                         @ColumnInfo val city : String,
                         @ColumnInfo val postCode : String,
                         @ColumnInfo val phoneNumber : String,
                         @ColumnInfo val mobileNumber : String,
                         @ColumnInfo val emailAddress : String,
                         @ColumnInfo val gender: Int) : Serializable