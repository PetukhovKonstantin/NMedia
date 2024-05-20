package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.entities.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)
    @Query("UPDATE PostEntity SET content = :text WHERE id = :id")
    suspend fun changeContentById(id: Long, text: String)
    suspend fun save(post: PostEntity) = if (post.id == 0L) insert(post) else changeContentById(post.id, post.content)

    @Query("UPDATE PostEntity SET\n" +
            "likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,\n" +
            "likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END\n" +
            "WHERE id = :id")
    suspend fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("UPDATE PostEntity SET shareCount = shareCount + 1 WHERE id = :id")
    suspend fun shareById(id: Long)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertOrUpdateDraft(draft: DraftEntity)
//    fun saveDraft(idUser: Long, content: String) = insertOrUpdateDraft(DraftEntity(idUser, content))
//
//    @Query("SELECT content FROM DraftEntity WHERE idUser = :idUser")
//    fun getDraft(idUser: Long): String?
}