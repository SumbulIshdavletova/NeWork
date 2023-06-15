package ru.sumbul.nework.user_page.domain.model

import ru.sumbul.nework.events.data.entity.AttachmentType

data class WallPosts(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
//($date-time)
    val link: String?,
    val likedByMe: Boolean,
    val attachment: PostWallAttachment?,
    val ownedByMe: Boolean,
) {
}

data class PostWallAttachment(
    val url: String,
    val attachmentType: AttachmentType?,
)