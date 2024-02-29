package ru.netology.nmedia.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
        private set(value) {
            field = value
            data.value = posts
        }
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {post ->
            if (post.id != id) post else post.copy(
                likedByMe = !post.likedByMe,
                likeCount = if (post.likedByMe) post.likeCount - 1 else post.likeCount + 1
            )
        }
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {post ->
            if (post.id != id) post else post.copy(
                shareCount = post.shareCount + 1
            )
        }
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
    }

    override fun saveDraft(content: String) {
        dao.saveDraft(content)
    }

    override fun getDraft(): String? = dao.getDraft()
}