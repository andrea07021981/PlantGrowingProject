package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.entity.DataCollectionEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPlantData(
    val id: Long,
    val plantId: Long,
    val temperature: Double,
    val humidity: Double
)

//**
// Convert Network results to database objects
//*
fun NetworkPlantData.asDomainModel(): DataCollectionDomain {
    return DataCollectionDomain(
        dataCollectionId = id,
        dataCollectionPlantId = plantId,
        dataCollectionHumidity = humidity,
        dataCollectionTemperature = temperature)
}

fun NetworkPlantData.asDatabaseModel(): DataCollectionEntity {
    return DataCollectionEntity(
        dataId = id,
        dataPlantId = plantId,
        dataTemperature = temperature,
        dataHumidity = humidity
    )
}
