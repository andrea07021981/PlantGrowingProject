package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.local.entity.PlantEntity
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
data class NetworkPlantContainer(val plants: List<NetworkPlant>)

fun NetworkPlantContainer.asDomainModel(): List<PlantDomain> {
    return plants.map {
        PlantDomain(
            plantId = it.id,
            plantUserId = it.userId,
            plantName = it.name,
            plantType = it.type,
            plantLastWater = it.lastWatering)
    }
}

fun NetworkPlantContainer.asDatabaseModel(): Array<PlantEntity> {
    return plants.map {
        PlantEntity(
            id = it.id,
            userId = it.userId,
            name = it.name,
            type = it.type,
            dataLastWatering = it.lastWatering)
    }.toTypedArray()
}
