package net.halowd.flutter_native_pedometer.walker.room

import androidx.room.*
import net.halowd.flutter_native_pedometer.walker.room.Walk

@Database(entities = [Walk::class], version = 1)
abstract class WalkDatabase : RoomDatabase() {
    abstract fun walkDao(): WalkDao
}