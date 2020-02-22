package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.entity.DataUserEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)

//**
// Convert Network results to database objects
//*
fun NetworkUser.asDomainModel(): UserDomain {
    return UserDomain(
        userId = id,
        userName = name,
        userSurname = surname,
        userEmail = email,
        userPassword = password)
}

fun NetworkUser.asDatabaseModel(): DataUserEntity {
    return DataUserEntity(
        dataId = id,
        dataName = name,
        dataSurname = surname,
        dataEmail = email,
        dataPassword = password
    )
}
