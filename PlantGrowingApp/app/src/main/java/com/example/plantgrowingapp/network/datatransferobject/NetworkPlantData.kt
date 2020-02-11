package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.entity.DataCollectionEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPlantData(
    val id: Long,
    val temperature: Double,
    val humidity: Double,
    @Json(name = "last_watering")val lastWatering: String
)

//**
// Convert Network results to database objects
//*
fun NetworkPlantData.asDomainModel(): DataCollectionDomain {
    return DataCollectionDomain(
        dataCollectionId = id,
        dataCollectionHumidity = humidity,
        dataCollectionTemperature = temperature,
        dataCollectionLastWater = lastWatering)
}

fun NetworkPlantData.asDatabaseModel(): DataCollectionEntity {
    return DataCollectionEntity(
        dataId = id,
        dataTemperature = temperature,
        dataHumidity = humidity,
        dataLastWatering = lastWatering
    )
}
