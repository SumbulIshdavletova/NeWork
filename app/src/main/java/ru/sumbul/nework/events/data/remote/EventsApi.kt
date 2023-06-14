package ru.sumbul.nework.events.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.sumbul.nework.events.data.entity.EventCreateRequestEntity
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.domain.model.Media

interface EventsApi {

    @GET("events/")
    suspend fun getEvents(): Response<List<EventResponse>>

    @POST("events/")
    suspend fun save(@Body event: EventCreate): Response<EventCreate>

    @GET("events/latest")
    suspend fun getEventsLatest(@Query("count") count: Int): Response<List<EventResponse>>

    @GET("events/{id}")
    suspend fun getById(@Path("id") id: Long): Response<EventResponse>

    @DELETE("events/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @GET("events/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<EventResponse>>

    @GET("events/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<EventResponse>>

    @POST("events/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<EventResponse>

    @DELETE("events/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<EventResponse>


    @GET("events/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<EventResponse>>

    @POST("events/{id}/participants")
    suspend fun addParticipantsById(@Path("id") id: Long): Response<EventResponse>

    @DELETE("events/{id}/participants")
    suspend fun removeParticipantsById(@Path("id") id: Long): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun upload(@Part file: MultipartBody.Part): Response<Media>


    // нужно ли добавлять / после ссылки в @

}