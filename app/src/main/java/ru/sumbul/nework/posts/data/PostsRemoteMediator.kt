package ru.sumbul.nework.posts.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.data.entity.toEntity
import ru.sumbul.nework.events.data.local.EventsDao
import ru.sumbul.nework.events.data.local.EventsRemoteKeyDao
import ru.sumbul.nework.events.data.remote.EventsApi
import ru.sumbul.nework.posts.data.entity.PostRemoteKeyEntity
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.posts.data.entity.toEntity
import ru.sumbul.nework.posts.data.local.PostsDao
import ru.sumbul.nework.posts.data.local.PostsRemoteKeyDao
import ru.sumbul.nework.posts.data.remote.PostApi


@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator (
        private val api: PostApi,
        private val postsDao: PostsDao,
        private val postsRemoteKeyDao: PostsRemoteKeyDao,
        private val appDb: AppDb
    ) : RemoteMediator<Int, PostResponseEntity>() {


        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, PostResponseEntity>
        ): MediatorResult {

            try {
                val result = when (loadType) {
                    LoadType.REFRESH -> api.getPostsLatest(state.config.initialLoadSize)
                    LoadType.PREPEND -> {
//                    return MediatorResult.Success(
//                        endOfPaginationReached = true
//                    )

                        val id = postsRemoteKeyDao.max() ?: return MediatorResult.Success(false)
                        api.getAfter(id, state.config.pageSize)
                    }
                    LoadType.APPEND -> {
                        val id = postsRemoteKeyDao.min() ?: return MediatorResult.Success(false)
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
                            postsRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(
                                        PostRemoteKeyEntity.KeyType.AFTER,
                                        body.first().id.toLong()
                                    ),
                                    PostRemoteKeyEntity(
                                        PostRemoteKeyEntity.KeyType.BEFORE,
                                        body.last().id.toLong()
                                    )
                                )
                            )
                            //     postDao.clear()
                        }
                        LoadType.PREPEND -> {
                            postsRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id.toLong()

                                )
                            )
                        }
                        LoadType.APPEND -> {
                            postsRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id.toLong()
                                )
                            )
                        }
                    }

                    appDb.postDao().upsertAll(body.toEntity())

                }

                return MediatorResult.Success(endOfPaginationReached = body.isEmpty())


            } catch (e: Exception) {
                return MediatorResult.Error(e)
            }
        }
    }