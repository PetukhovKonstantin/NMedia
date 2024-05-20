package ru.netology.nmedia.repositories

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
//    fun shareByIdAsync(id: Long, callback: Callback<Post>)
//    fun saveDraftAsync(idUser: Long, content: String, callback: Callback<Post>)
//    fun getDraftAsync(idUser: Long): String?
}