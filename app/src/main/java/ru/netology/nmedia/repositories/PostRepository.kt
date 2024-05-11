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

    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
    fun saveAsync(post: Post, callback: Callback<Post>)

//    interface GetAllCallback {
//        fun onSuccess(posts: List<Post>) {}
//        fun onError(e: Exception) {}
//    }
//    interface CallbackWithOutArgs {
//        fun onSuccess() {}
//        fun onError(e: Exception) {}
//    }

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}