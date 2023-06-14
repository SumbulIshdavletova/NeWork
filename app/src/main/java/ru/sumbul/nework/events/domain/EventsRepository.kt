package ru.sumbul.nework.events.domain


import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.domain.model.Media
import java.io.File


interface EventsRepository {
    // val data: Flow<PagingData<FeedItem>>
    val data: Flow<PagingData<EventResponse>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(event: EventCreate)
    suspend fun getEventById(id: Long) : EventResponse

    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun saveWithAttachment(event: EventCreate, file: File)
    suspend fun update()
    suspend fun upload(file: File): Media

}