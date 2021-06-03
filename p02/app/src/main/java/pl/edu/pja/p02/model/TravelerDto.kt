package pl.edu.pja.p02.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Traveler")
data class TravelerDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String?,
    val photoUri: String,
    val latitude: Double?,
    val longitude: Double?
)