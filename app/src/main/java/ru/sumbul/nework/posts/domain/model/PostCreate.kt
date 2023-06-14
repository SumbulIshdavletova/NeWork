package ru.sumbul.nework.posts.domain.model

import androidx.room.Embedded
import androidx.room.TypeConverters
import ru.sumbul.nework.posts.data.entity.PostAttachmentEmbeddable
import ru.sumbul.nework.posts.data.entity.PostCoordinatesEmbeddable
import ru.sumbul.nework.util.IntListTypeConverter

data class PostCreate(
    val id: Int,
    val content: String,
    val coords: PostCoordinates?,
    val link: String?,
    val attachment: PostAttachment?,
    @field:TypeConverters(IntListTypeConverter::class)
    val mentionIds: List<Int>?,
)