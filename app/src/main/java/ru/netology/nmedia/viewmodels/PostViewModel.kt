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
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(id: Long)  {
        val likedByMe = _data.value?.posts?.find { it.id == id }?.likedByMe ?: return
        repository.likeByIdAsync(id, likedByMe, object : PostRepository.CallbackWithOutArgs{
            override fun onSuccess() {
//                val posts = _data.value?.posts.orEmpty()
//                    .map { post ->
//                        if (post.id != id) post else post.copy(
//                            likedByMe = !likedByMe,
//                            likes = if (likedByMe) post.likes - 1 else post.likes + 1
//                        )
//                    }
//                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
        loadPosts()
    }
    fun shareById(id: Long) {
        // repository.shareById(id)
    }
    fun removeById(id: Long) {
        _data.postValue(_data.value?.copy(loading = true))
        repository.removeByIdAsync(id, object : PostRepository.CallbackWithOutArgs{
            override fun onSuccess() {
                val posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
    fun saveAndChangeContent(content: String) {
        edited.value?.let {
            if (content != it.content) {
                repository.saveAsync(it.copy(content = content), object : PostRepository.CallbackWithOutArgs{
                    override fun onSuccess() {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun saveDraft(content: String) {
        //repository.saveDraft(0L, content)
        TODO("Not yet implemented")
    }

    fun getDraft(): String?  {
        //repository.getDraft(0L)
        TODO("Not yet implemented")
    }
}