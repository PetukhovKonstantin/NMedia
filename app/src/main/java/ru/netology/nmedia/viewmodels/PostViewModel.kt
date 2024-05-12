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
    private val repository: PostRepository = PostRepositoryServerImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _error = SingleLiveEvent<Exception>()
    val error: LiveData<Exception>
        get() = _error

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                _error.postValue(e)
            }
        })
    }

    fun likeById(id: Long)  {
        val likedByMe = _data.value?.posts?.find { it.id == id }?.likedByMe ?: return
        _data.postValue(_data.value?.copy(loading = true))
        repository.likeByIdAsync(id, likedByMe, object : PostRepository.Callback<Post> {
            override fun onSuccess(resultPost: Post) {
                val posts = _data.value?.posts.orEmpty()
                    .map { post ->
                        if (post.id != id) post else resultPost
                    }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                _error.postValue(e)
            }
        })
    }

    fun removeById(id: Long) {
        _data.postValue(_data.value?.copy(loading = true))
        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(result: Unit) {
                val posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                _error.postValue(e)
            }
        })
    }
    fun saveAndChangeContent(content: String) {
        edited.value?.let {
            if (content != it.content) {
                repository.saveAsync(it.copy(content = content), object : PostRepository.Callback<Post> {
                    override fun onSuccess(posts: Post) {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                        _error.postValue(e)
                    }
                })
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }
}