package ru.netology.nmedia.dto

import kotlin.math.roundToInt

data class Post (
    val id: Int,
    val author: String,
    val content: String,
    var publiched: String,
    var likeCount: Int,
    var likedByMe: Boolean,
    var shareCount: Int
)
