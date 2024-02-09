package ru.netology.nmedia.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.services.PostService

interface PostActionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
}

class PostsAdapter(
    private val postAction: PostActionListener
) : RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(view, postAction)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val postAction: PostActionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding){
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(if (post.likedByMe) R.drawable.ic_like_active else R.drawable.ic_like)
            likeCount.text = PostService.ConvertCountToShortString(post.likeCount)
            shareCount.text = PostService.ConvertCountToShortString(post.shareCount)
            like.setOnClickListener {
                postAction.onLike(post)
            }
            share.setOnClickListener{
                postAction.onShare(post)
            }
        }
    }
}