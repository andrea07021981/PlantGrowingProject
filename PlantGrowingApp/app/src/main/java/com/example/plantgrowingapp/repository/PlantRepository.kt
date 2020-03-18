package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.CommandDomain
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.network.datatransferobject.NetworkCommand
import com.example.plantgrowingapp.network.datatransferobject.asDomainModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantRepository(
    private val database: PlantGrowingDatabase
) {
    suspend fun getNetworkPlant(userId: Long): List<PlantDomain> {
        return withContext(Dispatchers.IO) {
            val plantData = PlantApi.retrofitService.getPlantsByUser(userId).await()
            Log.d("Test data", plantData.plants[0].name.toString())
            plantData.asDomainModel()
            //database.dataCollectionDatabaseDao.insert(*plantData.asDatabaseModel())
        }
    }

    /**
     * New direct method added in retrofit 2.6. No need enqueue and retrofit with deferred
     * it's the same as PlantApi.retrofitService.postCommand(plantId = plantDomain.plantId, commandType = commandType).await().asDomainModel()
     * and retrofit methid deferred
     */
    suspend fun postCommand(plantDomain: PlantDomain, commandType: Int) =
        PlantApi.retrofitService.postCommand(plantId = plantDomain.plantId, commandType = commandType).asDomainModel()
}