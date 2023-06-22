package ru.sumbul.nework.posts.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.nework.events.data.entity.AttachmentEmbeddable
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.events.data.entity.CoordinatesEmbeddable
import ru.sumbul.nework.events.data.entity.EventResponseEntity
import ru.sumbul.nework.events.domain.model.Attachment
import ru.sumbul.nework.events.domain.model.Coordinates
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.posts.domain.model.PostAttachment
import ru.sumbul.nework.posts.domain.model.PostCoordinates
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.IntListTypeConverter

@Entity
data class PostResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
//($date-time)
    @Embedded
   val coords: PostCoordinatesEmbeddable?,
    val link: String?,
    @field:TypeConverters(IntListTypeConverter::class)
    val likeOwnerIds: List<Int>?,
    @field:TypeConverters(IntListTypeConverter::class)
    val mentionIds: List<Int>?,
    val mentionedByMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: PostAttachmentEmbeddable?,
    val ownedByMe: Boolean,
    // @field:TypeConverters(IntListTypeConverter::class)
    //  val users: List<UsersPreview>
) {
    fun toDto() = PostResponse(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        published,
       coords?.toDto(),
        link,
        likeOwnerIds,
        mentionIds,
        mentionedByMe,
        likedByMe,
        attachment?.toDto(),
        ownedByMe,
        //  users?.toDto(),
    )

    companion object {
        fun fromDto(dto: PostResponse) =
            PostResponseEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.authorJob,
                dto.content,
                dto.published,
              PostCoordinatesEmbeddable.fromDto(dto.coords),
                dto.link,
                dto.likeOwnerIds,
                dto.mentionIds,
                dto.mentionedByMe,
                dto.likedByMe,
                PostAttachmentEmbeddable.fromDto(dto.attachment),
                dto.ownedByMe,
                //        dto.users?.let { UsersEmbeddable.fromDto(it) },
            )

    }
}

data class PostCoordinatesEmbeddable(
    var lat: String,
    var longitude: String,
) {
    fun toDto() = PostCoordinates(lat, longitude)

    companion object {
        fun fromDto(dto: PostCoordinates?) = dto?.let {
            it.lat?.let { it1 -> it.long?.let { it2 -> PostCoordinatesEmbeddable(it1, it2) } }
        }
    }
}


data class PostAttachmentEmbeddable(
    var url: String,
    var attachmentType: AttachmentType?,
) {
    fun toDto() = PostAttachment(url, attachmentType)

    companion object {
        fun fromDto(dto: PostAttachment?) = dto?.let {
            PostAttachmentEmbeddable(it.url, it.attachmentType)
        }
    }
}

fun List<PostResponseEntity>.toDto(): List<PostResponse> = map(PostResponseEntity::toDto)
fun List<PostResponse>.toEntity(): List<PostResponseEntity> = map(PostResponseEntity::fromDto)


