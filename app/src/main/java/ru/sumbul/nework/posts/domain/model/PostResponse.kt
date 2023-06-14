package ru.sumbul.nework.posts.domain.model

import androidx.room.Embedded
import androidx.room.TypeConverters
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.posts.data.entity.PostAttachmentEmbeddable
import ru.sumbul.nework.posts.data.entity.PostCoordinatesEmbeddable
import ru.sumbul.nework.util.IntListTypeConverter

data class PostResponse(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
//($date-time)
    val coords: PostCoordinates?,
    val link: String?,
    @field:TypeConverters(IntListTypeConverter::class)
    val likeOwnerIds: List<Int>?,
    @field:TypeConverters(IntListTypeConverter::class)
    val mentionIds: List<Int>?,
    val mentionedByMe: Boolean,
    val likedByMe: Boolean,
    val attachment: PostAttachment?,
    val ownedByMe: Boolean,
// @field:TypeConverters(IntListTypeConverter::class)
//  val users: List<UsersPreview>
)

data class PostCoordinates(
    val lat: String,
    val longitude: String,
)

data class PostAttachment(
    val url: String,
    val attachmentType: AttachmentType?,
)