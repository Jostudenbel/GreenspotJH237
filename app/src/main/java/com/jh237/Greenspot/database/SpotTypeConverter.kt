package com.jh237.Greenspot.database

import android.net.Uri
import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class SpotTypeConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uri: String?): Uri? {
        return Uri.parse(uri)
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}