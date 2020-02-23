package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.bumptech.glide.load.engine.Resource
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.domain.asDatabaseModel
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.network.datatransferobject.asDatabaseModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class UserRepository(
    private val database: PlantGrowingDatabase
) {

    suspend fun saveNewUser(user: UserDomain) {
        withContext(Dispatchers.IO) {
            val postUser = PlantApi.retrofitService.postUser(user.userEmail, user.userPassword).await()
            database.userDatabaseDao.insert(postUser.asDatabaseModel())
        }
    }

    suspend fun getUser(user: UserDomain): LiveData<UserDomain?> {
        return withContext(Dispatchers.IO) {
            //TODO USE A COMPARE FOR HASH PASS. TEMPORARY METHOD USING ONLY THE EMAIL
            val userFound = database.userDatabaseDao.getUserByEmail(user.userEmail)
            Transformations.map(userFound) {
                it?.asDomainModel()
            }
        }
    }

    //TODO ADD THE NETWORK RETRIEVE FOR USERS USING
    //https://github.com/android/architecture-components-samples/blob/88747993139224a4bb6dbe985adf652d557de621/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
    suspend fun retrieveNetworkUser(user: UserDomain): LiveData<UserDomain?> {
        return withContext(Dispatchers.IO) {
            val netUser = PlantApi.retrofitService.getUser(user.userEmail, user.userPassword).await()
            database.userDatabaseDao.insert(netUser.asDatabaseModel())
            //TODO bcrypt is not secure, change here and on backend node
            val userFound = database.userDatabaseDao.getUser(user.userEmail, netUser.password)
            Transformations.map(userFound) {
                it?.asDomainModel()
            }
        }
    }
}