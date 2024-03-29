package ru.netology.nmedia.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entities.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {
//    private var posts = emptyList<Post>()
//        private set(value) {
//            field = value
//            data.value = posts
//        }
//    private val data = MutableLiveData(posts)

    init {
        //posts = dao.getAll()
    }

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {list ->
        list.map { it.toDto() }
    }
    override fun likeById(id: Long) = dao.likeById(id)

    override fun shareById(id: Long) = dao.shareById(id)

    override fun removeById(id: Long) = dao.removeById(id)

    override fun save(post: Post) = dao.save(PostEntity.fromDto(post))

    override fun saveDraft(idUser: Long, content: String) = dao.saveDraft(idUser, content)

    override fun getDraft(idUser: Long): String? = dao.getDraft(idUser)
}