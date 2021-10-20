package com.buildappswithvenkat.mytodoapp.data

import androidx.room.TypeConverter
import com.buildappswithvenkat.mytodoapp.data.models.Priority

class Convertor {

    @TypeConverter
    fun fromPriority(priority: Priority) : String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority : String) : Priority {
        return Priority.valueOf(priority)
    }
}