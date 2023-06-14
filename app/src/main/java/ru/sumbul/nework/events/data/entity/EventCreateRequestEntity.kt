package ru.sumbul.nework.events.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.util.IntListTypeConverter

@Entity
data class EventCreateRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val content: String,
    val datetime: String,
    @Embedded
    val coords: CoordinatesEmbeddable?,
    val type: Type?,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val link: String?,
    @field:TypeConverters(IntListTypeConverter::class)
    val speakerIds: List<Int>?,
) {
    fun toDto() = EventCreate(
        id,
        content,
        datetime,
        coords?.toDto(),
        type, attachment?.toDto(),
        link,
        speakerIds,
    )

    companion object {
        fun fromDto(dto: EventCreate) =
            EventCreateRequestEntity(
                dto.id,
                dto.content,
                dto.datetime,
                CoordinatesEmbeddable.fromDto(dto.coords),
                dto.type, AttachmentEmbeddable.fromDto(dto.attachment), dto.link,
                dto.speakerIds,
            )

    }
}

fun List<EventCreateRequestEntity>.toDto(): List<EventCreate> = map(EventCreateRequestEntity::toDto)
fun List<EventCreate>.toEntity(): List<EventCreateRequestEntity> =
    map(EventCreateRequestEntity::fromDto)