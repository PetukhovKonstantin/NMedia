package ru.netology.nmedia.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val shareCount: Int,
    val video: String? = null
) {
    fun toDto() = Post(id, author, content, published, likeCount, likedByMe, shareCount)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            id = post.id,
            author = post.author,
            content = post.content,
            published = post.published,
            likeCount = post.likes,
            likedByMe = post.likedByMe,
            shareCount = post.shareCount,
            video = post.video
        )
    }
}