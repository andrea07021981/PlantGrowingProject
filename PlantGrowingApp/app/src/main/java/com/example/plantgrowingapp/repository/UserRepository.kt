package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.domain.asDatabaseModel
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Console

class UserRepository(
    private val database: PlantGrowingDatabase
) {

    suspend fun saveNewUser(user: UserDomain) {
        withContext(Dispatchers.IO) {
            val newId = database.userDatabaseDao.insert(user.asDatabaseModel())
        }
    }

    suspend fun getUser(user: UserDomain): LiveData<UserDomain?> {
        return withContext(Dispatchers.IO) {
            val user = database.userDatabaseDao.getUser(user.userEmail, user.userPassword)
            //If null, check online
            if (user.value == null) {
                //TODO call the server for an online sign in and save data on db
            }
            Transformations.map(user) {
                it?.asDomainModel()
            }
        }
    }
}