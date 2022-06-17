package com.basemosama.movies.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
// class for converting genres list to json and vice versa
// it's better to use Room Relationships but for simplicity currently I used this way
// i will use relationship in the future when needed
class LongArrayTypeConverter {
    @TypeConverter
    fun fromLongArrayToJson(data: LongArray?): String? {
        return data?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun jsonToLongArray(data: String?): LongArray? {
        return data?.let {
            Gson().fromJson(it, LongArray::class.java)
        }
    }
}