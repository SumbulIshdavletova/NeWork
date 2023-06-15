package ru.sumbul.nework.user_page.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.sumbul.nework.posts.data.entity.PostRemoteKeyEntity
import ru.sumbul.nework.user_page.data.entity.WallPostRemoteKeyEntity

@Dao
interface WallPostsRemoteKeyDao {

    @Query("SELECT COUNT(*) == 0 FROM WallPostRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT MAX(id) FROM WallPostRemoteKeyEntity")
    suspend fun max(): Long?

    @Query("SELECT MIN(id) FROM WallPostRemoteKeyEntity")
    suspend fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallPostRemoteKeyEntity: WallPostRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallPostRemoteKeyEntity: List<WallPostRemoteKeyEntity>)

    @Query("DELETE FROM WallPostRemoteKeyEntity")
    suspend fun clear()
}