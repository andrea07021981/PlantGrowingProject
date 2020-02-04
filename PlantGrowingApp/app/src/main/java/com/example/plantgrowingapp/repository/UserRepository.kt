package com.example.plantgrowingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.domain.asDatabaseModel
import com.example.plantgrowingapp.local.entity.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            Transformations.map(user) {
                it?.asDomainModel()
            }
        }
    }
}