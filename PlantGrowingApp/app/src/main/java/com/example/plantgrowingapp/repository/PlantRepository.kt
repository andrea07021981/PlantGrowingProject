package com.example.plantgrowingapp.repository

import android.util.Log
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.network.datatransferobject.asDomainModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}