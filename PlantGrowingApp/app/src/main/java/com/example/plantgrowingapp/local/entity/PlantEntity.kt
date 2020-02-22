package com.example.plantgrowingapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plantgrowingapp.local.domain.PlantDomain


@Entity(tableName = "plant_table")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "userId")
    var userId: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "lastwatering")
    val dataLastWatering: String
)

fun PlantEntity.asDomainModel(): PlantDomain {
    return PlantDomain(
        plantId = id,
        plantUserId = userId,
        plantName = name,
        plantType = type,
        plantLastWater = dataLastWatering)
}
