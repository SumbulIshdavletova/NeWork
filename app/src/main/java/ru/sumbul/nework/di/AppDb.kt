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

@Database(
    entities = [EventResponseEntity::class, EventCreateRequestEntity::class, EventRemoteKeyEntity::class,
        PostResponseEntity::class, PostRemoteKeyEntity::class, PostCreateRequestEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun eventDao(): EventsDao
    abstract fun eventRemoteKeyDao(): EventsRemoteKeyDao
    abstract fun postDao(): PostsDao
    abstract fun postRemoteKeyDao(): PostsRemoteKeyDao
}