package com.example.plantgrowingapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.plantgrowingapp.local.entity.DataUserEntity

@Dao
interface UserDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DataUserEntity) : Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    fun update(user: DataUserEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM enduser_table")
    fun clear()

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from enduser_table WHERE dataId = :key")
    fun getUserWithId(key: Long): LiveData<DataUserEntity>

    /**
     * Selects and returns the user with given email and pass.
     */
    @Query("SELECT * from enduser_table WHERE email = :userEmail AND password = :userPassword")
    fun getUser(userEmail: String, userPassword: String): LiveData<DataUserEntity>
}