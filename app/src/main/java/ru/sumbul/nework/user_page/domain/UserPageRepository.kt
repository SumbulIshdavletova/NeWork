package ru.sumbul.nework.user_page.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.events.domain.model.Media
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.UserResponse
import ru.sumbul.nework.user_page.domain.model.WallPosts
import java.io.File

interface UserPageRepository {

    // val wall: Flow<PagingData<WallPosts>>
    suspend fun getAllWall(authorId: Long)
    fun getAllUsersWall(): MutableLiveData<List<WallPosts>?>?

    suspend fun getMyWall()
    fun getMyWallLiveData(): MutableLiveData<List<WallPosts>?>?

    //job
    suspend fun getJobs(userId: Long)
    fun getJobs(): MutableLiveData<List<Job>?>?
    suspend fun getMyJobs()
    fun getMyJobsLiveData(): MutableLiveData<List<Job>?>?
    suspend fun deleteJobById(jobId: Long)
    suspend fun save(job: Job)

    suspend fun getUser(userId: Long): UserResponse

    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun update()

}