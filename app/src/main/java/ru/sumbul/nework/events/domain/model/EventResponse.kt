package ru.sumbul.nework.events.domain.model

import androidx.room.TypeConverters
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.events.data.entity.Type
import ru.sumbul.nework.util.IntListTypeConverter
import ru.sumbul.nework.util.UserPreviewListTypeConverter

data class EventResponse(
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
    var coordinates: Coordinates?,
    val type: Type,
    @field:TypeConverters(IntListTypeConverter::class)
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean,
    @field:TypeConverters(IntListTypeConverter::class)
    val speakerIds: List<Int>?,
    @field:TypeConverters(IntListTypeConverter::class)
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean,
    var attachment: Attachment?,
    val link: String?,
    val ownedByMe: Boolean,
 //   var users: Users?
)

data class Coordinates(
    val lat: String,
    val longitude: String,
)

data class Attachment(
    val url: String,
    val attachmentType: AttachmentType?,
)


data class UserPreview(
    val name: String?,
    val avatar: String?
)

data class Users(
    @field:TypeConverters(UserPreviewListTypeConverter::class)
    var users: List<UserPreview?>?
)