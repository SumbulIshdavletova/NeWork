package ru.sumbul.nework.user_page.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.posts.data.entity.PostCreateRequestEntity
import ru.sumbul.nework.posts.data.entity.PostResponseEntity
import ru.sumbul.nework.user_page.data.entity.WallPostRemoteKeyEntity

@Dao
interface WallPostsDao {

    @Query("SELECT * FROM WallPostRemoteKeyEntity ORDER BY id DESC")
    fun getAll(): Flow<List<WallPostRemoteKeyEntity>>

    @Query("SELECT * FROM WallPostRemoteKeyEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, WallPostRemoteKeyEntity>

    @Query("SELECT * FROM WallPostRemoteKeyEntity WHERE id = :id")
    fun getPostById(id: Long): WallPostRemoteKeyEntity

    @Query("SELECT COUNT(*) == 0 FROM WallPostRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: WallPostRemoteKeyEntity)

    @Upsert
    suspend fun upsertAll(posts: List<WallPostRemoteKeyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<WallPostRemoteKeyEntity>)

    @Query("SELECT COUNT(*)  FROM WallPostRemoteKeyEntity")
    suspend fun count(): Int

    @Query("DELETE FROM WallPostRemoteKeyEntity WHERE id = :id")
    suspend fun removeById(id: Long)

//    @Query(
//        """
//        UPDATE WallPostRemoteKeyEntity SET
//        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
//        WHERE id = :id
//        """
//    )
//    suspend fun likeById(id: Long)

//    @Query("UPDATE PostResponseEntity SET show = 1 WHERE show = 0")
//    suspend fun update()

    @Query("DELETE FROM WallPostRemoteKeyEntity")
    suspend fun clear()
}