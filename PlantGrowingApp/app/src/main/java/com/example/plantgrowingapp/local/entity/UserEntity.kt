package com.example.plantgrowingapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plantgrowingapp.local.domain.UserDomain


@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "surname")
    val surname: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String
)

fun UserEntity.asDomainModel(): UserDomain {
    return UserDomain(
        userName = name,
        userSurname = surname,
        userEmail = email,
        userPassword = password)
}
