package ru.sumbul.nework.posts.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.sumbul.nework.posts.data.entity.PostCreateRequestEntity
import ru.sumbul.nework.posts.data.entity.PostResponseEntity

@Dao
interface PostsDao {

    @Query("SELECT * FROM PostResponseEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostResponseEntity>>

    @Query("SELECT * FROM PostResponseEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, PostResponseEntity>

    @Query("SELECT * FROM PostResponseEntity WHERE id = :id")
    fun getPostById(id: Long): PostResponseEntity

    @Query("SELECT COUNT(*) == 0 FROM PostResponseEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostCreateRequestEntity)

    @Upsert
    suspend fun upsertAll(posts: List<PostResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostResponseEntity>)

    @Query("SELECT COUNT(*)  FROM PostResponseEntity")
    suspend fun count(): Int

    @Query("DELETE FROM PostResponseEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query(
        """
        UPDATE PostResponseEntity SET
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun likeById(id: Long)

//    @Query("UPDATE PostResponseEntity SET show = 1 WHERE show = 0")
//    suspend fun update()

    @Query("DELETE FROM PostResponseEntity")
    suspend fun clear()
}