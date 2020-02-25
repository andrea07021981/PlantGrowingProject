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

    suspend fun postCommand(plantDomain: PlantDomain, commandType: Int): LiveData<CommandDomain?> {
        return withContext(Dispatchers.IO) {
            val networkCommandResponse = MutableLiveData<NetworkCommand>()
            PlantApi.retrofitService.postCommand(plantId = plantDomain.plantId, commandType = commandType).enqueue(object: Callback<NetworkCommand> {
                override fun onFailure(call: Call<NetworkCommand>, t: Throwable) {
                    networkCommandResponse.value = null
                }
                override fun onResponse(
                    call: Call<NetworkCommand>,
                    response: Response<NetworkCommand>
                ) {
                    networkCommandResponse.value = response.body()
                }
            })
            // Synchronously return LiveData
            // Its value will be available onResponse
            Transformations.map(networkCommandResponse) {
                it.asDomainModel()
            }
        }
    }
}