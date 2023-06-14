package ru.sumbul.nework.posts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKeyEntity(
    @PrimaryKey
    val type: KeyType,
    val id: Long,

    ) {

    enum class KeyType {
        AFTER,
        BEFORE,
    }
}