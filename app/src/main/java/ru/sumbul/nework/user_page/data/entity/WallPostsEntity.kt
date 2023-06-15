package ru.sumbul.nework.user_page.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import ru.sumbul.nework.events.data.entity.AttachmentType
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.posts.domain.model.PostCoordinates
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.user_page.domain.model.PostWallAttachment
import ru.sumbul.nework.user_page.domain.model.WallPosts

@Entity
data class WallPostsEntity(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
//($date-time)
    val link: String?,
    val likedByMe: Boolean,
    @Embedded
    val attachment: PostWallAttachmentEmbeddable?,
    val ownedByMe: Boolean,
) {

    fun toDto() = WallPosts(
        id, authorId,
        author, authorAvatar, content, published,
        link, likedByMe, attachment?.toDto(), ownedByMe
    )

    companion object {
        fun fromDto(dto: WallPosts) =
            WallPostsEntity(
                dto.id, dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.link,
                dto.likedByMe,
                PostWallAttachmentEmbeddable.fromDto(dto.attachment),
                dto.ownedByMe
            )
    }

}

data class PostWallAttachmentEmbeddable(
    var url: String,
    var attachmentType: AttachmentType?,
) {
    fun toDto() = PostWallAttachment(url, attachmentType)

    companion object {
        fun fromDto(dto: PostWallAttachment?) = dto?.let {
            PostWallAttachmentEmbeddable(it.url, it.attachmentType)
        }
    }
}

fun List<WallPostsEntity>.toDto(): List<WallPosts> = map(WallPostsEntity::toDto)
fun List<WallPosts>.toEntity(): List<WallPostsEntity> = map(WallPostsEntity::fromDto)


