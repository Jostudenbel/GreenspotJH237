package com.jh237.Greenspot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// This file is the database access object used by SpotRepo to access the database.

@Dao
interface SpotDAO {
    @Query("SELECT * FROM spot")
    fun getAll(): Flow<List<Spot>>

    @Query("SELECT * FROM spot WHERE id = :id")
    fun getGreenspot(id: UUID): Spot

    @Update
    fun updateGreenspot(spot: Spot)

    @Insert
    fun addGreenspot(spot: Spot)
}