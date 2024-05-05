package ru.netology.nmedia.repositories
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun saveDraft(idUser: Long, content: String)
    fun getDraft(idUser: Long): String?
}