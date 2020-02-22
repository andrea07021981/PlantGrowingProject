package com.example.plantgrowingapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plantgrowingapp.local.domain.UserDomain


@Entity(tableName = "enduser_table")
data class DataUserEntity(
    @PrimaryKey(autoGenerate = true)
    var dataId: Long = 0L,

    @ColumnInfo(name = "name")
    val dataName: String,

    @ColumnInfo(name = "surname")
    val dataSurname: String,

    @ColumnInfo(name = "email")
    var dataEmail: String,

    @ColumnInfo(name = "password")
    var dataPassword: String
)

fun DataUserEntity.asDomainModel(): UserDomain {
    return UserDomain(
        userId = dataId,
        userName = dataName,
        userSurname = dataSurname,
        userEmail = dataEmail,
        userPassword = dataPassword)
}
