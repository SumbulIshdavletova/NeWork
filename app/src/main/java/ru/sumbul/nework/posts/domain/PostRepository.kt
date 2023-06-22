package ru.sumbul.nework.posts.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.events.domain.model.Media
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import java.io.File

interface PostRepository {

    // val data: Flow<PagingData<FeedItem>>
    val data: Flow<PagingData<PostResponse>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: PostCreate)
    suspend fun getPostById(id: Long) : PostResponse

    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun saveWithAttachment(post: PostCreate, file: File)
    suspend fun update()
    suspend fun upload(file: File): Media
}