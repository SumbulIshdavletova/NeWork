package ru.sumbul.nework.posts.data

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.error.NetworkError
import ru.sumbul.nework.events.data.EventsRemoteMediator
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.domain.model.Attachment
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.domain.model.Media
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.posts.data.local.PostsDao
import ru.sumbul.nework.posts.data.local.PostsRemoteKeyDao
import ru.sumbul.nework.posts.data.remote.PostApi
import ru.sumbul.nework.posts.domain.PostRepository
import ru.sumbul.nework.posts.domain.model.PostAttachment
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import java.io.File
import java.io.IOException
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

    override suspend fun save(post: PostCreate) {
        try {
            val response = api.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun getPostById(id: Long): PostResponse {
        var post: PostResponse
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
        try {
            val response = api.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            val response = api.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            postDao.likeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun unlikeById(id: Long) {
        try {
            val response = api.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            postDao.likeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: PostCreate, file: File) {
        try {
            val upload = upload(file)
            val postWithAttachment =
                post.copy(attachment = PostAttachment(upload.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }

    override suspend fun upload(file: File): Media {
        try {
            val data = MultipartBody.Part.createFormData(
                "file", file.name, file.asRequestBody()
            )
            val response = api.upload(data)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }
}