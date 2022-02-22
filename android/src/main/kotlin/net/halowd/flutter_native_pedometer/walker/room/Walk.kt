package net.halowd.flutter_native_pedometer.walker.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Walk(
    @PrimaryKey(autoGenerate = true) val idx: Int,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "create_at") val createAt: Long
)