package com.example.plantgrowingapp.local.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
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
    val dataLastWatering: String
)


fun DataCollectionEntity.asDomainModel(): DataCollectionDomain {
    return DataCollectionDomain(
        dataCollectionTemperature = dataTemperature,
        dataCollectionHumidity = dataHumidity,
        dataCollectionLastWater = dataLastWatering)
}

fun List<DataCollectionEntity>.asListDomainModel(): List<DataCollectionDomain> {
    return map {
        DataCollectionDomain(
            dataCollectionId = it.dataId,
            dataCollectionTemperature = it.dataTemperature,
            dataCollectionHumidity = it.dataHumidity,
            dataCollectionLastWater = it.dataLastWatering
        )
    }
}
