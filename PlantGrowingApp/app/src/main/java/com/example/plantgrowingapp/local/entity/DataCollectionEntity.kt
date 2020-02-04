package com.example.plantgrowingapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import java.sql.Date

@Entity(tableName = "data_collection_table")
data class DataCollectionEntity(

    @PrimaryKey(autoGenerate = true)
    var dataId: Long = 0L,

    @ColumnInfo(name = "temperature")
    val dataTemperature: Double,

    @ColumnInfo(name = "humidity")
    val dataHumidity: Double,

    @ColumnInfo(name = "lastwatering")
    val dataLastWatering: Long
)


fun DataCollectionEntity.asDomainModel(): DataCollectionDomain {
    return DataCollectionDomain(
        dataCollectionTemperature = dataTemperature,
        dataCollectionHumidity = dataHumidity,
        dataCollectionLastWater = dataLastWatering)
}
