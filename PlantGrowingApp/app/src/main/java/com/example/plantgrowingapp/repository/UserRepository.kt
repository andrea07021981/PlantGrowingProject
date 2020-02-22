package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.domain.asDatabaseModel
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.network.datatransferobject.asDatabaseModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.Console

class UserRepository(
    private val database: PlantGrowingDatabase
) {

    suspend fun saveNewUser(user: UserDomain) {
        withContext(Dispatchers.IO) {
            //val postUser = PlantApi.retrofitService.postUser(user.userEmail, user.userPassword).await()
            database.userDatabaseDao.insert(user.asDatabaseModel())
        }
    }

    suspend fun getUser(user: UserDomain): LiveData<UserDomain?> {
        return withContext(Dispatchers.IO) {
            var userFound = database.userDatabaseDao.getUser(user.userEmail, user.userPassword)
            //If null, check online and save for online
            if (userFound.value == null) {
                val netUser = PlantApi.retrofitService.getUser(user.userEmail, user.userPassword).await()
                database.userDatabaseDao.insert(netUser.asDatabaseModel())
                //TODO user bcrypt like backend
                userFound = database.userDatabaseDao.getUser(user.userEmail, netUser.password)
            }
            Transformations.map(userFound) {
                it?.asDomainModel()
            }
        }
    }
}