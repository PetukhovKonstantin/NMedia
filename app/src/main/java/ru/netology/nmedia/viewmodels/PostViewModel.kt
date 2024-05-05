package ru.netology.nmedia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repositories.PostRepository
import ru.netology.nmedia.repository.PostRepositoryServerImpl
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    shareCount = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryServerImpl() //PostRepositoryRoomImpl(AppDb.getInstance(application).postDao) //PostRepositoryFilesImpl(application)
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

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
            // Оптимистичная модель
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
    fun saveAndChangeContent(content: String) {
        edited.value?.let {
            if (content != it.content) {
                thread {
                    repository.save(it.copy(content = content))
                    _postCreated.postValue(Unit)
                }
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun saveDraft(content: String) {
        //thread { repository.saveDraft(0L, content) }
        TODO("Not yet implemented")
    }

    fun getDraft(): String?  {
        //repository.getDraft(0L)
        TODO("Not yet implemented")
    }
}