package ru.sumbul.nework.user_page.data.entity

import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.UserResponse

data class UserResponseEntity(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?
) {

    fun toDto() = UserResponse(
        id, login,
        name, avatar
    )

    companion object {
        fun fromDto(dto: UserResponse) =
            UserResponseEntity(
                dto.id, dto.login, dto.name,
                dto.avatar
            )
    }

}

fun List<UserResponseEntity>.toDto(): List<UserResponse> = map(UserResponseEntity::toDto)
fun List<UserResponse>.toEntity(): List<UserResponseEntity> = map(UserResponseEntity::fromDto)

