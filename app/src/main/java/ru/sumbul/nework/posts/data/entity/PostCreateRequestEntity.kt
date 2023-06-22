package ru.sumbul.nework.posts.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.nework.events.data.entity.AttachmentEmbeddable
import ru.sumbul.nework.events.data.entity.CoordinatesEmbeddable
import ru.sumbul.nework.events.data.entity.EventCreateRequestEntity
import ru.sumbul.nework.events.domain.model.Coordinates
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.util.IntListTypeConverter

@Entity
data class PostCreateRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val content: String,
    @Embedded
    val coords: PostCoordinatesEmbeddable?,
    val link: String?,
    @Embedded
    val attachment: PostAttachmentEmbeddable?,
    @field:TypeConverters(IntListTypeConverter::class)
    val mentionIds: List<Int>?,
) {

    fun toDto() = PostCreate(
        id,
        content,
         coords?.toDto(),
        link, attachment?.toDto(),mentionIds
    )

    companion object {
        fun fromDto(dto: PostCreate) =
            PostCreateRequestEntity(
                dto.id,
                dto.content,
                PostCoordinatesEmbeddable.fromDto(dto.coords),
                dto.link, PostAttachmentEmbeddable.fromDto(dto.attachment),
                dto.mentionIds
            )

    }

}

fun List<PostCreateRequestEntity>.toDto(): List<PostCreate> = map(PostCreateRequestEntity::toDto)
fun List<PostCreate>.toEntity(): List<PostCreateRequestEntity> = map(PostCreateRequestEntity::fromDto)


