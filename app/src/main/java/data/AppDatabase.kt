package com.example.licznikplus1.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ReviewEventEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewEventDao(): ReviewEventDao
}