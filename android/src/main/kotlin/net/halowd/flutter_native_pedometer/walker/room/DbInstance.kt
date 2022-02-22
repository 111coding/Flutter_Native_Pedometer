package net.halowd.flutter_native_pedometer.walker.room

import android.content.Context
import androidx.room.Room


object DbInstance {
    private var walkDatabase : WalkDatabase? = null

    @JvmStatic
    fun walkDatabase(applicationContext : Context)  : WalkDatabase{
        if(walkDatabase == null){
            walkDatabase = Room.databaseBuilder(
                applicationContext,
                WalkDatabase::class.java, "walker_database"
            ).allowMainThreadQueries().build()
        }
        return walkDatabase!!
    }

}