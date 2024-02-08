package ru.netology.nmedia.dto

data class Post (
    val id: Int,
    val author: String,
    val content: String,
    val publiched: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val shareCount: Int
)
