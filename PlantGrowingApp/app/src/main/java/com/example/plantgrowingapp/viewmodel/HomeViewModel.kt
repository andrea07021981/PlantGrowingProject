package com.example.plantgrowingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.CommandDomain
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.repository.PlantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val user: UserDomain
) : AndroidViewModel(application) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _plantList = MutableLiveData<List<PlantDomain>>()
    val plantList: LiveData<List<PlantDomain>>
        get() = _plantList

    private var _navigateToChart = MediatorLiveData<PlantDomain>()
    val navigateToChart: LiveData<PlantDomain>
        get() = _navigateToChart

    private var _command = MutableLiveData<CommandDomain>()
    val command: LiveData<CommandDomain>
        get() = _command

    private val database = PlantGrowingDatabase.getInstance(application)
    private val plantRepository = PlantRepository(database)

    init {
        //TODO REPLACE CURRENT LIST WITH PAGING LIBRARY
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        _plantList.value = plantRepository.getNetworkPlant(userId = user.userId)
    }

    fun moveToChart(plant: PlantDomain) {
        _navigateToChart.value = plant
    }

    fun doneToChart() {
        _navigateToChart.value = null
    }

    fun sendCommandWater(plant: PlantDomain) = viewModelScope.launch {
        _command.value = plantRepository.postCommand(plant, 1).value
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing HomeViewModel with parameter
     */
    class Factory(var app: Application, var user: UserDomain) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app, user) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}