package com.jh237.Greenspot.database

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

//Declares the databases name on startup

const val DATABASE_NAME = "greenspot-database"

//SpotRepo is uses the SpotDAO class to access the database so that the rest of the Application can use SpotRepo as an intermediary.


/*
Declares the database class that extends RoomDatabase and defines the DAO for the database.
The coroutineScope is used to launch coroutines in the database that run concurrently with the rest of the application.
These functions use the DAO or database access object to perform database operations.
 */
class SpotRepo private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: SpotDatabase = Room.databaseBuilder(
        context.applicationContext,
        SpotDatabase::class.java,
        DATABASE_NAME
    ).allowMainThreadQueries().build()

    //SpotDatabase is initialized with the database.

    companion object {
        private var INSTANCE: SpotRepo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SpotRepo(context)
            }
        }

        fun get(): SpotRepo {
            return INSTANCE ?: throw IllegalStateException("SpotRepo must be initialized")
        }

    }

    //companion object is used to initialize the INSTANCE variable.

    fun getSpots(): Flow<List<Spot>> = database.spotDAO.getAll()

    //getSpots is used to get all the spots from the database.

    suspend fun getSpot(id: UUID): Spot = database.spotDAO.getGreenspot(id)

    //getSpot is used to get a specific spot from the database by searching for its id.
    fun updateSpot(spot: Spot) {
        coroutineScope.launch {
            database.spotDAO.updateGreenspot(spot)
        }
    }

    //updateSpot is used to update a specific spot in the database.
    fun addSpot(spot: Spot) {
        coroutineScope.launch {
            database.spotDAO.addGreenspot(spot)
        }
    }
    //addSpot is used to add a new spot to the database.

}