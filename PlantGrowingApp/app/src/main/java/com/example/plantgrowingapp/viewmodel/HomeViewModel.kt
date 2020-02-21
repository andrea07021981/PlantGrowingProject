package com.example.plantgrowingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewmodeljob = SupervisorJob()
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewmodelScope = CoroutineScope(viewmodeljob + Dispatchers.Main)


    private val database = PlantGrowingDatabase.getInstance(application)
    private val dataRepository = DataRepository(database)

    private var _dataCollection = MutableLiveData<List<DataCollectionDomain>>()
    val dataCollection: LiveData<List<DataCollectionDomain>>
        get() = _dataCollection

    init {
        loadData()
    }

    private fun loadData() = viewmodelScope.launch {
        dataRepository.refreshOnlineData()
        _dataCollection.value = dataRepository.getData().value
    }

    override fun onCleared() {
        super.onCleared()
        viewmodeljob.cancel()
    }
}