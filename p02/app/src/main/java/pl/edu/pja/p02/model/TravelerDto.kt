package pl.edu.pja.p02.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Traveler")
data class TravelerDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo()
    val description: String,
    val photoId: Long
)
