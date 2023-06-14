package ru.sumbul.nework.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.events.data.local.EventsDao
import ru.sumbul.nework.events.data.local.EventsRemoteKeyDao
import ru.sumbul.nework.posts.data.local.PostsDao
import ru.sumbul.nework.posts.data.local.PostsRemoteKeyDao

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun provideEventDao(db: AppDb): EventsDao = db.eventDao()

    @Provides
    fun provideEventRemoteKeyDao(db: AppDb): EventsRemoteKeyDao = db.eventRemoteKeyDao()

    @Provides
    fun providePostDao(db: AppDb): PostsDao = db.postDao()

    @Provides
    fun providePostRemoteKeyDao(db: AppDb): PostsRemoteKeyDao = db.postRemoteKeyDao()


}