package ru.sumbul.nework.events.data

import androidx.paging.*
import androidx.room.withTransaction
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.events.data.entity.EventRemoteKeyEntity
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.data.entity.toEntity
import ru.sumbul.nework.events.data.local.EventsDao
import ru.sumbul.nework.events.data.local.EventsRemoteKeyDao
import ru.sumbul.nework.events.data.remote.EventsApi
import ru.sumbul.nework.di.AppDb


@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
    private val api: EventsApi,
    private val eventsDao: EventsDao,
    private val eventsRemoteKeyDao: EventsRemoteKeyDao,
    private val appDb: AppDb
) : RemoteMediator<Int, EventResponseEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventResponseEntity>
    ): MediatorResult {

        try {
            val result = when (loadType) {
                LoadType.REFRESH -> api.getEventsLatest(state.config.initialLoadSize)
                LoadType.PREPEND -> {
//                    return MediatorResult.Success(
//                        endOfPaginationReached = true
//                    )

                    val id = eventsRemoteKeyDao.max() ?: return MediatorResult.Success(false)
                    api.getAfter(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = eventsRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    api.getBefore(id, state.config.pageSize)
                }
            }

            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }
            val body = result.body() ?: throw ApiError(
                result.code(),
                result.message(),
            )

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        //       postRemoteKeyDao.clear()
                        eventsRemoteKeyDao.insert(
                            listOf(
                                EventRemoteKeyEntity(
                                    EventRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id.toLong()
                                ),
                                EventRemoteKeyEntity(
                                    EventRemoteKeyEntity.KeyType.BEFORE,
                                    body.last().id.toLong()
                                )
                            )
                        )
                        //     postDao.clear()
                    }
                    LoadType.PREPEND -> {
                        eventsRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                EventRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id.toLong()

                            )
                        )
                    }
                    LoadType.APPEND -> {
                        eventsRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                EventRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id.toLong()
                            )
                        )
                    }
                }

                appDb.eventDao().upsertAll(body.toEntity())

            }

            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())


        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}