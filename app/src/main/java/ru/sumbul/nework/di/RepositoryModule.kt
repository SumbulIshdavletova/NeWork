package ru.sumbul.nework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.nework.events.data.EventsRepositoryImpl
import ru.sumbul.nework.events.domain.EventsRepository
import ru.sumbul.nework.posts.data.PostRepositoryImpl
import ru.sumbul.nework.posts.domain.PostRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsEventRepository(impl: EventsRepositoryImpl): EventsRepository


    @Binds
    @Singleton
    abstract fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository


}
