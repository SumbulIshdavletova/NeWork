package ru.sumbul.nework.user_page.domain.model

data class UserResponse(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?
) {
}