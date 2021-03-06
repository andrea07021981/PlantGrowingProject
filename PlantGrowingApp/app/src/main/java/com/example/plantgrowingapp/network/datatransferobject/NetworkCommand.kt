package com.example.plantgrowingapp.network.datatransferobject

import com.example.plantgrowingapp.local.domain.CommandDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCommand(
    val id: Long,
    @Json(name = "plant_id") val plantId: Long,
    @Json(name = "command_type") val commandType: Int,
    val executed: Boolean
)

/**
 * Convert Network results to domain app objects
 */
fun NetworkCommand.asDomainModel(): CommandDomain {
    return CommandDomain(
        commandId = id,
        commandPlantId = plantId,
        commandCommandType = commandType,
        commandExecuted = executed)
}

