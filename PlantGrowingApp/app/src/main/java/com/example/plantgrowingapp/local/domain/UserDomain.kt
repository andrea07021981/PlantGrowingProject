package com.example.plantgrowingapp.local.domain

import android.os.Parcelable
import com.example.plantgrowingapp.local.entity.UserEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDomain(
    var userName: String = "",
    var userSurname: String = "",
    var userEmail: String = "",
    var userPassword: String = ""
) : Parcelable {

    constructor() : this("", "","")
}

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        name = userName,
        surname = userSurname,
        email = userEmail,
        password = userPassword)
}