package ru.netology.nmedia.repositories
import android.content.Context
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun saveDraft(idUser: Long, content: String)
    fun getDraft(idUser: Long): String?
}