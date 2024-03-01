package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.entities.DraftEntity
import ru.netology.nmedia.entities.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)
    @Query("UPDATE PostEntity SET content = :text WHERE id = :id")
    fun changeContentById(id: Long, text: String)
    fun save(post: PostEntity) = if (post.id == 0L) insert(post) else changeContentById(post.id, post.content)

    @Query("UPDATE PostEntity SET\n" +
            "likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,\n" +
            "likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END\n" +
            "WHERE id = :id")
    fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("UPDATE PostEntity SET shareCount = shareCount + 1 WHERE id = :id")
    fun shareById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateDraft(draft: DraftEntity)
    fun saveDraft(idUser: Long, content: String) = insertOrUpdateDraft(DraftEntity(idUser, content))

    @Query("SELECT content FROM DraftEntity WHERE idUser = :idUser")
    fun getDraft(idUser: Long): String?
}