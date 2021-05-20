package pl.edu.pja.p02

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.edu.pja.p02.model.TravelerDto

@Database(
    entities = [
        TravelerDto::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val travelers: TravelerDao
}