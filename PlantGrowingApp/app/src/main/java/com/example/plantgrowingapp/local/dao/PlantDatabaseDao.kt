package com.example.plantgrowingapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.plantgrowingapp.local.entity.PlantEntity

@Dao
interface PlantDatabaseDao {

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from plant_table WHERE id = :key and userId = :fkUser")
    fun getPlantsByUser(key: Long, fkUser: Long): LiveData<PlantEntity>
}