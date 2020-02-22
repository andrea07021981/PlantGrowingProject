package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.local.entity.PlantEntity
import com.example.plantgrowingapp.local.entity.UserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPlant(
    val id: Long,
    @Json(name = "user_id")val userId: Long,
    val name: String,
    val type: String,
    @Json(name = "last_watering")val lastWatering: String
)

/**
* Convert Network results to domain app objects
*/
fun NetworkPlant.asDomainModel(): PlantDomain {
    return PlantDomain(
        plantId = id,
        plantUserId = userId,
        plantName = name,
        plantType = type,
        plantLastWater = lastWatering)
}

/**
 * Convert Network results to database objects
 */
fun NetworkPlant.asDatabaseModel(): PlantEntity {
    return PlantEntity(
        id = id,
        userId = userId,
        name = name,
        type = type,
        dataLastWatering = lastWatering)
}
