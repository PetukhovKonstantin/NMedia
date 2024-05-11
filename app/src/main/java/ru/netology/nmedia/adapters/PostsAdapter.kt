package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.services.PostService
import ru.netology.nmedia.utils.AndroidUtils.setAllOnClickListener


interface PostActionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlayVideo(post: Post)
    fun onOpenPost(post: Post)
}

class PostsAdapter(private val postAction: PostActionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(view, postAction)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val postAction: PostActionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.setText(post.likes.toString())
            share.setText(PostService.ConvertCountToShortString(post.shareCount))
            groupVideo.visibility = if (post.video.isNullOrEmpty()) View.GONE else View.VISIBLE

            Glide.with(avatar)
                .load("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(R.dimen.avatar_rounded_corners)))
                .into(avatar)

            imageAttachment.visibility = if (post.attachment?.url.isNullOrEmpty()) View.GONE else View.VISIBLE
            if (imageAttachment.visibility == View.VISIBLE) {
                Glide.with(imageAttachment)
                    .load("${BuildConfig.BASE_URL}/images/${post.attachment?.url}")
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(imageAttachment)
            }

            like.setOnClickListener {
                postAction.onLike(post)
            }

            share.setOnClickListener {
                postAction.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                postAction.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                postAction.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            groupVideo.setAllOnClickListener {
                if (!post.video.isNullOrBlank()) {
                    postAction.onPlayVideo(post)
                }
            }

            groupOpenPost.setAllOnClickListener {
                postAction.onOpenPost(post)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}