package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.local.entity.asListDomainModel
import com.example.plantgrowingapp.network.datatransferobject.asDatabaseModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val database: PlantGrowingDatabase
) {

    suspend fun refreshOnlineData() {
        withContext(Dispatchers.IO) {
            val plantData = PlantApi.retrofitService.getPlantData().await()
            Log.d("Test data", plantData.infodata[0].temperature.toString())
            database.dataCollectionDatabaseDao.insert(*plantData.asDatabaseModel())
        }
    }

    suspend fun getData(): LiveData<List<DataCollectionDomain>> {
        return withContext(Dispatchers.IO) {
            Transformations.map(database.dataCollectionDatabaseDao.getAllData()) {
                it.asListDomainModel()
            }
        }
    }
}