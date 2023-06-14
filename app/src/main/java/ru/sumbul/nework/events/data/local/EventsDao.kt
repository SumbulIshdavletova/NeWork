package ru.sumbul.nework.events.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.events.data.entity.EventCreateRequestEntity
import ru.sumbul.nework.events.data.entity.EventResponseEntity

@Dao
interface EventsDao {

    @Query("SELECT * FROM EventResponseEntity ORDER BY id DESC")
    fun getAll(): Flow<List<EventResponseEntity>>

    @Query("SELECT * FROM EventResponseEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EventResponseEntity>


    @Query("SELECT * FROM EventResponseEntity WHERE id = :id")
    fun getEventById(id: Long): EventResponseEntity

    @Query("SELECT COUNT(*) == 0 FROM EventResponseEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventCreateRequestEntity)

    @Upsert
    suspend fun upsertAll(events: List<EventResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventResponseEntity>)

    @Query("SELECT COUNT(*)  FROM EventResponseEntity")
    suspend fun count(): Int

//    @Query("DELETE FROM EventResponseEntity WHERE id = :id")
//    suspend fun removeById(id: Long)
//
//    @Query(
//        """
//        UPDATE EventResponseEntity SET
//        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
//        WHERE id = :id
//        """
//    )
//    suspend fun likeById(id: Long)
// просто писать что не работае без интернета
//    @Query("UPDATE EventResponseEntity SET show = 1 WHERE show = 0")
//    suspend fun update()

    @Query("DELETE FROM EventResponseEntity")
    suspend fun clear()

}