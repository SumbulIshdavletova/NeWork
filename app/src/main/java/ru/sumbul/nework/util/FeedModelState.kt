package ru.sumbul.nework.util

data class FeedModelState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,

    val error: Boolean = false,
    val likeError: Boolean = false,
    val saveError: Boolean = false,
    val removeError: Boolean = false
)