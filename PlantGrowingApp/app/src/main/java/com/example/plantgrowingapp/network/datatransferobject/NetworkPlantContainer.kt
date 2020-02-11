package com.example.plantgrowingapp.network.datatransferobject

import androidx.lifecycle.Transformations.map
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.entity.DataCollectionEntity
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * Plant data Holder holds a list of Data.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "Data": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkPlantDataContainer(val infodata: List<NetworkPlantData>)

fun NetworkPlantDataContainer.asDomainModel(): List<DataCollectionDomain> {
    return infodata.map {
        DataCollectionDomain(
            dataCollectionId = it.id,
            dataCollectionTemperature = it.temperature,
            dataCollectionHumidity = it.humidity,
            dataCollectionLastWater = it.lastWatering)
    }
}

fun NetworkPlantDataContainer.asDatabaseModel(): Array<DataCollectionEntity> {
    return infodata.map {
        DataCollectionEntity(
            dataId = it.id,
            dataTemperature = it.temperature,
            dataHumidity = it.humidity,
            dataLastWatering = it.lastWatering)
    }.toTypedArray()
}
