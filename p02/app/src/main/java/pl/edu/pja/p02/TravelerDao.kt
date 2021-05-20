package pl.edu.pja.p02

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.edu.pja.p02.model.TravelerDto

@Dao
interface TravelerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(traveler: TravelerDto) : Long

    @Query("SELECT * FROM Traveler;")
    fun getAll() : List<TravelerDto>
}