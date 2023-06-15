package ru.sumbul.nework.di

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.nework.events.data.entity.EventCreateRequestEntity
import ru.sumbul.nework.events.data.entity.EventRemoteKeyEntity
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.data.local.EventsDao
import ru.sumbul.nework.events.data.local.EventsRemoteKeyDao
import ru.sumbul.nework.posts.data.entity.PostCreateRequestEntity
import ru.sumbul.nework.posts.data.entity.PostRemoteKeyEntity
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.posts.data.local.PostsDao
import ru.sumbul.nework.posts.data.local.PostsRemoteKeyDao
import ru.sumbul.nework.user_page.data.entity.JobEntity
import ru.sumbul.nework.user_page.data.entity.UserResponseEntity
import ru.sumbul.nework.user_page.data.entity.WallPostRemoteKeyEntity
import ru.sumbul.nework.user_page.data.entity.WallPostsEntity
import ru.sumbul.nework.user_page.data.local.WallPostsDao
import ru.sumbul.nework.user_page.data.local.WallPostsRemoteKeyDao
import ru.sumbul.nework.user_page.domain.model.UserResponse

@Database(
    entities = [EventResponseEntity::class, EventCreateRequestEntity::class, EventRemoteKeyEntity::class,
        PostResponseEntity::class, PostRemoteKeyEntity::class, PostCreateRequestEntity::class,
        JobEntity::class, UserResponseEntity::class,
        WallPostRemoteKeyEntity::class, WallPostsEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun eventDao(): EventsDao
    abstract fun eventRemoteKeyDao(): EventsRemoteKeyDao
    abstract fun postDao(): PostsDao
    abstract fun postRemoteKeyDao(): PostsRemoteKeyDao
    abstract fun wallPostsDao(): WallPostsDao
    abstract fun wallRemoteKeyDao(): WallPostsRemoteKeyDao
}