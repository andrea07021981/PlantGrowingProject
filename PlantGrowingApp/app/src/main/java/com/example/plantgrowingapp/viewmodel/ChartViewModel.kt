package com.example.plantgrowingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.plantgrowingapp.constant.*
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

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

    private val _chartStatus = MutableLiveData<ApiCallStatus>()
    val chartStatus: LiveData<ApiCallStatus>
        get() = _chartStatus

    init {
        loadDataPlant()
    }

    private fun loadDataPlant() = viewModelScope.launch {
        try {
            _chartStatus.value = Loading()
            _infoData.value = dataRepository.getNetworkData(plantId = plant.plantId)
            _chartStatus.value = Done()
        } catch (e: ConnectException) {
            e.printStackTrace()
            _chartStatus.value = Error()
        }
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