package com.jh237.Greenspot.database

import androidx.room.Database
import androidx.room.TypeConverters

// Defines the database class that extends RoomDatabase and defines the DAO for the database.

@Database(entities = [Spot::class], version = 1, exportSchema = false)
@TypeConverters(SpotTypeConverter::class)
abstract class SpotDatabase : androidx.room.RoomDatabase() {
    abstract val spotDAO: SpotDAO
}