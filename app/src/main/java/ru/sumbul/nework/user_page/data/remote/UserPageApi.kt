package ru.sumbul.nework.user_page.data.remote


import androidx.room.Upsert
import retrofit2.Response
import retrofit2.http.*
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.UserResponse
import ru.sumbul.nework.user_page.domain.model.WallPosts

interface UserPageApi {

    //other people wall
    @GET("{author_id}/wall")
    suspend fun getAllWall(@Path("author_id") authorId: Long): Response<List<WallPosts>>

    @GET("{author_id}/wall/latest")
    suspend fun getWallLatest(
        @Path("author_id") authorId: Long,
        @Query("count") count: Int
    ): Response<List<WallPosts>>

    @GET("{author_id}/wall/{post_id}/after")
    suspend fun getAfter(
        @Path("author_id") authorId: Long,
        @Path("post_id") postId: Long,
        @Query("count") count: Int
    ): Response<List<WallPosts>>

    @GET("{author_id}/wall/{post_id}/before")
    suspend fun getBefore(
        @Path("author_id") authorId: Long,
        @Path("post_id") postId: Long,
        @Query("count") count: Int
    ): Response<List<WallPosts>>

    @GET("{author_id}/posts/{post_id}/newer")
    suspend fun getNewer(
        @Path("author_id") authorId: Long,
        @Path("post_id") postId: Long,
    ): Response<List<WallPosts>>

    //job

    @GET("{user_id}/wall")
    suspend fun getUserJobs(@Path("user_id") userId: Long): Response<List<Job>>

    //user
    @GET("users/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Long,
    ): Response<UserResponse>

    //likes
    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<PostResponse>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<PostResponse>


}