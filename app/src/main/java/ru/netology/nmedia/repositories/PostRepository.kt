package ru.netology.nmedia.repositories
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
    fun saveAsync(post: Post, callback: Callback<Post>)
//    fun shareByIdAsync(id: Long, callback: Callback<Post>)
//    fun saveDraftAsync(idUser: Long, content: String, callback: Callback<Post>)
//    fun getDraftAsync(idUser: Long): String?

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}