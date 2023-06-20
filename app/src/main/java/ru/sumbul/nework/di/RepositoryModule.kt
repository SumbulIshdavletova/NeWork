package ru.sumbul.nework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.nework.auth.data.AuthRepositoryImpl
import ru.sumbul.nework.auth.domain.AuthRepository
import ru.sumbul.nework.events.data.EventsRepositoryImpl
import ru.sumbul.nework.events.domain.EventsRepository
import ru.sumbul.nework.posts.data.PostRepositoryImpl
import ru.sumbul.nework.posts.domain.PostRepository
import ru.sumbul.nework.user_page.data.UserPageRepositoryImpl
import ru.sumbul.nework.user_page.domain.UserPageRepository
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

    @Binds
    @Singleton
    abstract fun binsUserPageRepository(impl: UserPageRepositoryImpl): UserPageRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}
