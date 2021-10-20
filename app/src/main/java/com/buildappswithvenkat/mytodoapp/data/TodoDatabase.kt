package com.buildappswithvenkat.mytodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.buildappswithvenkat.mytodoapp.data.models.TodoData

@Database(entities = [TodoData::class], version = 1, exportSchema = false)
@TypeConverters(Convertor::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun toDoDao() : ToDoDao

    companion object{
        @Volatile
        private var INSTANCE : TodoDatabase? = null

        fun getDataBase(context: Context) : TodoDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null)
                return tempInstance

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}