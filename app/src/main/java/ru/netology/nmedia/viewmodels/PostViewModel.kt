package ru.netology.nmedia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repositories.PostRepository
import ru.netology.nmedia.repositories.PostRepositoryInMemoryImpl

val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likeCount = 0,
    likedByMe = false,
    shareCount = 0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun saveAndChangeContent(content: String) {
        edited.value?.let {
            if (content != it.content) {
                repository.save(it.copy(content = content))
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }
}