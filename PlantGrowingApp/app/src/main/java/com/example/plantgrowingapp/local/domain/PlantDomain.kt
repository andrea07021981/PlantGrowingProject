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
    var plantImgUrl: String = "https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/plant.png",//TODO ADD DB FIELD
    var plantLastWater: String = ""
) : Parcelable {

    constructor() : this(0L, 0L, "", "","", "")
}

fun PlantDomain.asDatabaseModel(): PlantEntity {
    return PlantEntity(
        id = plantId,
        userId = plantUserId,
        name = plantName,
        type = plantType,
        dataLastWatering = plantLastWater)
}