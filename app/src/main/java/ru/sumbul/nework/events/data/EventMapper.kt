package ru.sumbul.nework.events.data

import ru.sumbul.nework.events.domain.model.Attachment
import ru.sumbul.nework.events.domain.model.Coordinates
import ru.sumbul.nework.events.domain.model.EventCreate
import ru.sumbul.nework.events.domain.model.EventResponse
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapResponseToCreate(response: EventResponse): EventCreate {
        return EventCreate(
            id = response.id,
            content = response.content,
            response.datetime,
            coords = response.coordinates?.lat?.let {
                response.coordinates?.longitude?.let { it1 ->
                    Coordinates(
                        it,
                        it1
                    )
                }
            },
            response.type,
            attachment = response.attachment?.url?.let {
                response.attachment?.attachmentType?.let { it1 ->
                    Attachment(
                        url = it,
                        attachmentType = it1
                    )
                }
            },
            link = response.link,
            speakerIds = response.speakerIds
        )
    }
}