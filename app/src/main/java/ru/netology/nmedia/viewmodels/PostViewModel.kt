package ru.netology.nmedia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repositories.PostRepository
import ru.netology.nmedia.repositories.PostRepositoryRoomImpl

val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likeCount = 0,
    likedByMe = false,
    shareCount = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(application).postDao) //PostRepositoryFilesImpl(application)
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

    fun saveDraft(content: String) {
        repository.saveDraft(0L, content)
    }

    fun getDraft(): String? = repository.getDraft(0L)
}