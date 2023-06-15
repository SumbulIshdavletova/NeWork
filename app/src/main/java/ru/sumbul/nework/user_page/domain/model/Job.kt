package ru.sumbul.nework.user_page.domain.model


data class Job(
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,
)