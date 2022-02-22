package net.halowd.flutter_native_pedometer.walker.room

import androidx.room.*
import net.halowd.flutter_native_pedometer.walker.room.Walk
import java.sql.Date

@Dao
interface WalkDao {

    @Query("SELECT * FROM walk")
    fun getAll() : List<Walk>

    @Query("SELECT COUNT(*) FROM walk WHERE create_at >= :basedate")
    fun getRecent(basedate: Long): Int

    @Insert
    fun insert(walk : Walk)


    fun insert(count : Int) {
        insert(
            Walk(idx = 0,count = count, createAt = System.currentTimeMillis())
        )
    }

}
