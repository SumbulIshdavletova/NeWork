package ru.sumbul.nework.posts.domain

import ru.sumbul.nework.posts.domain.model.PostAttachment
import ru.sumbul.nework.posts.domain.model.PostCoordinates
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapResponseToCreate(response: PostResponse): PostCreate {
        return PostCreate(
            id = response.id,
            content = response.content,
            coords = PostCoordinates(lat = response.coords?.lat, long = response.coords?.long),
            link = response.link,
            attachment = response.attachment?.url?.let {
                PostAttachment(
                    url = it,
                    attachmentType = response.attachment.attachmentType
                )
            },
            mentionIds = response.mentionIds
        )
    }
}