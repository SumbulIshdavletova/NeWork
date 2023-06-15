package ru.sumbul.nework.user_page.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WallPostRemoteKeyEntity(
    @PrimaryKey
    val type: KeyType,
    val id: Long,

    ) {

    enum class KeyType {
        AFTER,
        BEFORE,
    }
}