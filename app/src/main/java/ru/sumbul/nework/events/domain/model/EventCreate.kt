package ru.sumbul.nework.events.domain.model

import androidx.room.TypeConverters
import ru.sumbul.nework.events.data.entity.Type
import ru.sumbul.nework.util.IntListTypeConverter


data class EventCreate (
    val id: Int,
    val content: String,
    val datetime: String,
    val coords: Coordinates?,
    val type: Type?,
    val attachment: Attachment?,
    val link: String?,
    @field:TypeConverters(IntListTypeConverter::class)
    val speakerIds: List<Int>?,
)

data class Media(val id: String)