package com.johndeweydev.xecret.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converter {
  @TypeConverter
  fun fromString(value: String?): ArrayList<String> {
    val listType = object : TypeToken<ArrayList<String>>() {}.type
    return Gson().fromJson(value, listType)
  }

  @TypeConverter
  fun fromArrayList(list: ArrayList<String>): String {
    return Gson().toJson(list)
  }

  @TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time
  }
}