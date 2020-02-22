package com.example.plantgrowingapp.local.domain

import android.os.Parcelable
import com.example.plantgrowingapp.local.entity.UserEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDomain(
    var userId: Long = 0L,
    var userName: String = "",
    var userSurname: String = "",
    var userEmail: String = "",
    var userPassword: String = ""
) : Parcelable {

    constructor() : this(0L, "", "","")
}

fun UserDomain.asDatabaseModel(): UserEntity {
    return UserEntity(
        id = userId,
        name = userName,
        surname = userSurname,
        email = userEmail,
        password = userPassword)
}