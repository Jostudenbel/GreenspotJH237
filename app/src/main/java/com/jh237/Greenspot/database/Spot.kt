package com.jh237.Greenspot.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Spot(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var place: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: String = ""
)
