package ru.netology.nmedia.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repositories.PostRepository
import ru.netology.nmedia.repositories.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
}