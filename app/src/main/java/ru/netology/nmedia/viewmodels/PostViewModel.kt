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


    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            // Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            try {
                // Данные успешно получены
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                // Получена ошибка
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun likeById(id: Long)  {
        thread {
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map { post ->
                        if (post.id != id) post else post.copy(
                            likedByMe = !post.likedByMe,
                            likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                        )
                    }
                )
            )
            repository.likeById(id)
        }
    }
    fun shareById(id: Long) {
        thread {  repository.shareById(id) }
    }
    fun removeById(id: Long) {
        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
