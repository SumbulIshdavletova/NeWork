package ru.sumbul.nework.user_page.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.di.AppDb
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.error.NetworkError
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.user_page.data.local.WallPostsDao
import ru.sumbul.nework.user_page.data.local.WallPostsRemoteKeyDao
import ru.sumbul.nework.user_page.data.remote.UserPageApi
import ru.sumbul.nework.user_page.domain.UserPageRepository
import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.UserResponse
import ru.sumbul.nework.user_page.domain.model.WallPosts
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPageRepositoryImpl @Inject constructor(
    private val dao: WallPostsDao,
    private val keyDao: WallPostsRemoteKeyDao,
    private val api: UserPageApi,
    private val appDb: AppDb,
) : UserPageRepository {

    private var usersWall: MutableLiveData<List<WallPosts>?>? =
        MutableLiveData<List<WallPosts>?>()

    override fun getAllUsersWall(): MutableLiveData<List<WallPosts>?>? {
        return usersWall
    }

    private var myWall: MutableLiveData<List<WallPosts>?>? =
        MutableLiveData<List<WallPosts>?>()

    override fun getMyWallLiveData(): MutableLiveData<List<WallPosts>?>? {
        return myWall
    }

    override suspend fun getMyWall() {
        var result: List<WallPosts>?
        try {
            result = api.getMyWall().body()
            myWall?.value = result
        } catch (e: Exception) {
            result = emptyList()
            myWall?.value = result
        }
    }

    override suspend fun getAllWall(authorId: Long) {
        var result: List<WallPosts>?
        try {
            result = api.getAllWall(authorId).body()
            usersWall?.value = result
        } catch (e: Exception) {
            result = emptyList()
            usersWall?.value = result
        }
    }

    private var jobs: MutableLiveData<List<Job>?>? =
        MutableLiveData<List<Job>?>()

    override fun getJobs(): MutableLiveData<List<Job>?>? {
        return jobs
    }

    override suspend fun getJobs(userId: Long) {
        var result: List<Job>?
        try {
            result = api.getUserJobs(userId).body()
            jobs?.value = result
        } catch (e: Exception) {
            result = emptyList()
            jobs?.value = result
        }
    }

    override suspend fun getMyJobs() {
        var result: List<Job>?
        try {
            result = api.getMyJobs().body()
            myJobs?.value = result
        } catch (e: Exception) {
            result = emptyList()
            myJobs?.value = result
        }
    }

    private var myJobs: MutableLiveData<List<Job>?>? =
        MutableLiveData<List<Job>?>()

    override fun getMyJobsLiveData(): MutableLiveData<List<Job>?>? {
        return myJobs
    }

    override suspend fun deleteJobById(jobId: Long) {
        try {
            val response = api.deleteJob(jobId)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun save(job: Job) {
        try {
            val response = api.setJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun getUser(userId: Long): UserResponse {
        var user: UserResponse = UserResponse(0, "no info", "no info", "no info")
        try {
            val response = api.getUser(userId)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            user = body
        } catch (e: Exception) {
            ru.sumbul.nework.error.UnknownError
        }
        return user
    }

    override suspend fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun unlikeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }
}