package ru.sumbul.nework.posts.data

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.events.data.EventsRemoteMediator
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.domain.model.Media
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.posts.data.local.PostsDao
import ru.sumbul.nework.posts.data.local.PostsRemoteKeyDao
import ru.sumbul.nework.posts.data.remote.PostApi
import ru.sumbul.nework.posts.domain.PostRepository
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postDao: PostsDao,
    private val api: PostApi,
    private val postRemoteKeyDao: PostsRemoteKeyDao,
    private val appDb: AppDb,
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<PostResponse>> = Pager(
        config = PagingConfig(pageSize = 2),
        remoteMediator = PostsRemoteMediator(api, postDao, postRemoteKeyDao, appDb),
        pagingSourceFactory = postDao::getPagingSource,
    ).flow.map {
        it.map(PostResponseEntity::toDto)
    }

    override suspend fun getAll() {
        TODO("Not yet implemented")
    }

    override fun getNewerCount(id: Long): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun save(event: PostCreate) {
        TODO("Not yet implemented")
    }

    override suspend fun getPostById(id: Long): PostResponse {
        var post : PostResponse
        try {
            val response = api.getById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            //  dao.upsert(body)
            post = body
        } catch (e: Exception) {
            post = postDao.getPostById(id).toDto()
        }
        return post
    }

    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun unlikeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun saveWithAttachment(event: PostCreate, file: File) {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }

    override suspend fun upload(file: File): Media {
        TODO("Not yet implemented")
    }
}