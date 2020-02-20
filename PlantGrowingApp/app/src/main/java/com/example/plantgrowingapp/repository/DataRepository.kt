package com.example.plantgrowingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.entity.asDomainModel
import com.example.plantgrowingapp.local.entity.asListDomainModel
import com.example.plantgrowingapp.network.datatransferobject.asDatabaseModel
import com.example.plantgrowingapp.network.service.PlantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class DataRepository(
    private val database: PlantGrowingDatabase
) {

    private var _dataCollection = MutableLiveData<List<DataCollectionDomain>>()
    val dataCollection: LiveData<List<DataCollectionDomain>>
        get() = _dataCollection

    /**
     * Refresh the data stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshOnlineData() {
        withContext(Dispatchers.IO) {
            val plantData = PlantApi.retrofitService.getPlantData().await()
            Log.d("Test data", plantData.infodata[0].temperature.toString())
            database.dataCollectionDatabaseDao.insert(*plantData.asDatabaseModel())
        }
    }

    /*TEST IF WITH ONLY A VARIABLE LILE THE LINK THE NOTIFICATION TO VM HAPPENS, EVEN RIGHT AFTER THE WORKer call the network and save on db
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
    No, use a val list livedata, like
    https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html#8*/
    suspend fun getData(): LiveData<List<DataCollectionDomain>> {
        return withContext(Dispatchers.IO) {
            Transformations.map(database.dataCollectionDatabaseDao.getAllData()) {
                it.asListDomainModel()
            }
        }
    }
}