package com.example.plantgrowingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.network.datatransferobject.asDatabaseModel
import com.example.plantgrowingapp.network.datatransferobject.asDomainModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val database: PlantGrowingDatabase
) {

    suspend fun postNetworkUser(user: UserDomain) {
        withContext(Dispatchers.IO) {
            val postUser = PlantApi.retrofitService.postUser(user.userEmail, user.userPassword).await()
        }
    }

    suspend fun getNetworkUser(user: UserDomain): UserDomain? {
        return withContext(Dispatchers.IO) {
            val netUser = PlantApi.retrofitService.getUser(user.userEmail, user.userPassword).await()
            netUser.asDomainModel()
        }
    }
}