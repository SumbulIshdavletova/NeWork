package ru.sumbul.nework.posts.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.events.domain.model.Media

interface PostApi {
    @GET("posts/")
    suspend fun getPosts(): Response<List<PostResponse>>

    @POST("posts/")
    suspend fun save(@Body event: PostCreate): Response<PostCreate>

    @GET("posts/latest")
    suspend fun getPostsLatest(@Query("count") count: Int): Response<List<PostResponse>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<PostResponse>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<PostResponse>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<PostResponse>


    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<PostResponse>>

    @Multipart
    @POST("media")
    suspend fun upload(@Part file: MultipartBody.Part): Response<Media>


}