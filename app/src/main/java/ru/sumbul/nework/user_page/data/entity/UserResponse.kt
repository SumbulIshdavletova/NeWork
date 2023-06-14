package ru.sumbul.nework.user_page.data.entity

data class UserResponse(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?
) {
}