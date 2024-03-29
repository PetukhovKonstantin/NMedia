package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val shareCount: Int,
    val video: String? = null
)
