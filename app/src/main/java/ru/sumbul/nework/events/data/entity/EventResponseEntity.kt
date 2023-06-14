package ru.sumbul.nework.events.data.entity

import androidx.room.*
import ru.sumbul.nework.events.domain.model.*
import ru.sumbul.nework.util.IntListTypeConverter
import ru.sumbul.nework.util.UserPreviewListTypeConverter

@Entity
data class EventResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    //($date-time)
    val published: String,
//($date-time)
    @Embedded
    var coordinates: CoordinatesEmbeddable?,
    val type: Type,
    @field:TypeConverters(IntListTypeConverter::class)
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean,
    @field:TypeConverters(IntListTypeConverter::class)
    val speakerIds: List<Int>?,
    @field:TypeConverters(IntListTypeConverter::class)
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    val link: String?,
    val ownedByMe: Boolean,
   // @Embedded
 //   var users: UsersEmbeddable?
) {


    fun toDto() = EventResponse(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        datetime,
        published,
        coordinates?.toDto(),
        type,
        likeOwnerIds,
        likedByMe,
        speakerIds,
        participantsIds,
        participatedByMe,
        attachment?.toDto(),
        link,
        ownedByMe,
      //  users?.toDto(),
    )

    companion object {
        fun fromDto(dto: EventResponse) =
            EventResponseEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.authorJob,
                dto.content,
                dto.datetime,
                dto.published,
                CoordinatesEmbeddable.fromDto(dto.coordinates),
                dto.type,
                dto.likeOwnerIds,
                dto.likedByMe,
                dto.speakerIds,
                dto.participantsIds,
                dto.participatedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment),
                dto.link,
                dto.ownedByMe,
        //        dto.users?.let { UsersEmbeddable.fromDto(it) },
            )

    }
}

data class CoordinatesEmbeddable(
    var lat: String,
    var longitude: String,
) {
    fun toDto() = Coordinates(lat, longitude)

    companion object {
        fun fromDto(dto: Coordinates?) = dto?.let {
            CoordinatesEmbeddable(it.lat, it.longitude)
        }
    }
}



data class AttachmentEmbeddable(
    var url: String,
    var attachmentType: AttachmentType?,
) {
    fun toDto() = Attachment(url, attachmentType)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.attachmentType)
        }
    }
}

data class UsersEmbeddable(
    @field:TypeConverters(UserPreviewListTypeConverter::class)
    var users: List<UserPreview?>?
) {
    fun toDto() = Users(users)

    companion object {
        fun fromDto(dto: Users) = UsersEmbeddable(dto.users)

    }
}

enum class Type {
    OFFLINE, ONLINE
}

enum class AttachmentType {
    IMAGE, VIDEO, AUDIO
}

fun List<EventResponseEntity>.toDto(): List<EventResponse> = map(EventResponseEntity::toDto)
fun List<EventResponse>.toEntity(): List<EventResponseEntity> = map(EventResponseEntity::fromDto)
