package com.example.plantgrowingapp.local.domain

import android.os.Parcelable
import com.example.plantgrowingapp.local.entity.PlantEntity
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PlantDomain(
    var plantId: Long = 0L,
    var plantUserId: Long = 0L,
    var plantName: String = "",
    var plantType: String = "",
    var plantLastWater: String = ""
) : Parcelable {

    constructor() : this(0L, 0L, "", "","")
}

fun PlantDomain.asDatabaseModel(): PlantEntity {
    return PlantEntity(
        id = plantId,
        userId = plantUserId,
        name = plantName,
        type = plantType,
        dataLastWatering = plantLastWater)
}