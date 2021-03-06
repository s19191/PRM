package pl.edu.pja.p02

import androidx.room.*
import pl.edu.pja.p02.model.TravelerDto

@Dao
interface TravelerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(traveler: TravelerDto) : Long

    @Query("SELECT * FROM Traveler;")
    fun getAll() : List<TravelerDto>

    @Query("SELECT * FROM Traveler WHERE photoUri = :photoUri")
    fun getByPhotoUri(photoUri: String ) : TravelerDto

    @Query("SELECT * FROM Traveler WHERE id = :id")
    fun getById(id: Long) : TravelerDto

    @Query("UPDATE Traveler SET description = :description WHERE id = :id")
    fun update(id: Long, description: String)

    @Query("UPDATE Traveler SET description = :description WHERE photoUri = :photoUri")
    fun update(photoUri: String, description: String)

    @Query("DELETE FROM Traveler WHERE id = :id")
    fun delete(id: Long)
}