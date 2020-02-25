package com.example.plantgrowingapp.local.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommandDomain(
    var commandId: Long = 0,
    var commandPlantId: Long = 0,
    var commandCommandType: Int = 0,
    var commandExecuted: Boolean = false
) : Parcelable {
    constructor() : this(0, 0,0, false)
}