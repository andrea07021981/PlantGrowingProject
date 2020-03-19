package com.example.plantgrowingapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.repository.DataRepository
import com.google.android.gms.nearby.connection.Payload
import retrofit2.HttpException
import java.lang.Exception

/**
 * This class update the local db with the online backend when available
 */
class RefreshDataWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params){
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = PlantGrowingDatabase.getInstance(context = applicationContext)
        val repository = DataRepository(database)
        return try {
            repository.refreshOnlineData()
            Result.Success.success()
        } catch (e: HttpException) {
            Result.Failure.failure()
        } catch (e: Exception) {
            Result.Failure.failure()
        }
    }
}