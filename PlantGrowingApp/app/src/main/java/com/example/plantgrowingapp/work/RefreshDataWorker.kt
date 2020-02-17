package com.example.plantgrowingapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.repository.DataRepository
import retrofit2.HttpException

class RefreshDataWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params){
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Payload {
        val database = PlantGrowingDatabase.getInstance(context = applicationContext)
        val repository = DataRepository(database)
        return try {
            repository.refreshOnlineData()
            Payload(Result.SUCCESS)
        } catch (e: HttpException) {
            Payload(Result.FAILURE)
        }
    }
}