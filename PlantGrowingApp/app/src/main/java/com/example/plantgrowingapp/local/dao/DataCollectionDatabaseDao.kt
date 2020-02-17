package com.example.plantgrowingapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.plantgrowingapp.local.entity.DataCollectionEntity

@Dao
interface DataCollectionDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dataCollection: DataCollectionEntity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param data new value to write
     */
    @Update
    fun update(user: DataCollectionEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM data_collection_table")
    fun clear()

    /**
     * Selects and returns all data.
     */
    @Query("SELECT * from data_collection_table")
    fun getAllData(): LiveData<List<DataCollectionEntity>>

    //TODO add get data by date
}