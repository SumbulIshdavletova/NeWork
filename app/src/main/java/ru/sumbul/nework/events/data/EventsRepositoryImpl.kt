package ru.sumbul.nework.events.data

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.error.AppError
import ru.sumbul.nework.error.NetworkError
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.events.data.entity.EventCreateRequestEntity
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.data.entity.toEntity
import ru.sumbul.nework.events.data.local.EventsDao
import ru.sumbul.nework.events.data.local.EventsRemoteKeyDao
import ru.sumbul.nework.events.data.remote.EventsApi
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.events.domain.EventsRepository
import ru.sumbul.nework.events.domain.model.Attachment
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.domain.model.Media
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepositoryImpl @Inject constructor(
    private val eventsDao: EventsDao,
    private val api: EventsApi,
    private val eventsRemoteKeyDao: EventsRemoteKeyDao,
    private val appDb: AppDb
) : EventsRepository {


    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<EventResponse>> = Pager(
        config = PagingConfig(pageSize = 2),
        remoteMediator = EventsRemoteMediator(api, eventsDao, eventsRemoteKeyDao, appDb),
        pagingSourceFactory = eventsDao::getPagingSource,
    ).flow.map {
        it.map(EventResponseEntity::toDto)
    }

    override suspend fun getAll() {
        try {
            val response = api.getEvents()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventsDao.upsertAll(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = api.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventsDao.insert(body.toEntity())
            emit(body.size)
        }
    }.catch { e -> throw AppError.from(e) }.flowOn(Dispatchers.Default)

    override suspend fun update() {
        try {
            val response = api.getEvents()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
//            if (eventsDao.count() > 0) {
//                eventsDao.update()
//            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }


    override suspend fun save(event: EventCreate) {
        try {
            val response = api.save(event)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventsDao.insert(EventCreateRequestEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun getEventById(id: Long): EventResponse {
        var eventResponse : EventResponse
        try {
            val response = api.getById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
          //  dao.upsert(body)
            eventResponse = body
        } catch (e: Exception) {
            eventResponse = eventsDao.getEventById(id).toDto()
        }
        return eventResponse
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = api.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            eventsDao.removeById(id)
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
            eventsDao.removeById(id)
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
            eventsDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun saveWithAttachment(event: EventCreate, file: File) {
        try {
            val upload = upload(file)
            val postWithAttachment =
                event.copy( attachment = Attachment(upload.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
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