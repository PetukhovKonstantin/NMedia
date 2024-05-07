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

    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(id: Long, callback: CallbackWithOutArgs)
    fun removeByIdAsync(id: Long, callback: CallbackWithOutArgs)
    fun saveAsync(post: Post, callback: CallbackWithOutArgs)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }
    interface CallbackWithOutArgs {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }
}