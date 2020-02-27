package com.example.plantgrowingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChartViewModel(
    application: Application,
    private val plant: PlantDomain
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = PlantGrowingDatabase.getInstance(application)
    private val dataRepository = DataRepository(database)

    private val _infoData = MutableLiveData<List<DataCollectionDomain?>>()
    val infoData: LiveData<List<DataCollectionDomain?>>
        get() = _infoData

    init {
        loadDataPlant()
    }

    private fun loadDataPlant() = viewModelScope.launch {
        _infoData.value = dataRepository.getNetworkData(plantId = plant.plantId)
    }
    /**
     * Factory for constructing ChartViewModel with parameter
     */
    class Factory(
        private val application: Application,
        private val plant: PlantDomain) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChartViewModel(application = application, plant = plant) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}