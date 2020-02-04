package com.example.plantgrowingapp.local.domain

import android.os.Parcelable
import com.example.plantgrowingapp.local.entity.DataCollectionEntity
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class DataCollectionDomain(
    var dataCollectionTemperature: Double = 0.0,
    var dataCollectionHumidity: Double = 0.0,
    var dataCollectionLastWater: Long = 0
) : Parcelable {

    constructor() : this(0.0, 0.0, 0)
}

fun DataCollectionDomain.asDatabaseModel(): DataCollectionEntity {
    return DataCollectionEntity(
        dataTemperature = dataCollectionTemperature,
        dataHumidity = dataCollectionHumidity,
        dataLastWatering = dataCollectionLastWater)
}