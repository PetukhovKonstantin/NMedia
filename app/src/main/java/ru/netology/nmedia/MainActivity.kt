package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.PostService
import ru.netology.nmedia.dto.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) {
            post ->
            with(binding){
                author.text = post.author
                published.text = post.publiched
                content.text = post.content
                like.setImageResource(if (post.likedByMe) R.drawable.ic_like_active else R.drawable.ic_like)
                likeCount.text = PostService.ConvertCountToShortString(post.likeCount)
                shareCount.text = PostService.ConvertCountToShortString(post.shareCount)
            }
        }

        with(binding) {
            like.setOnClickListener {
                viewModel.like()
            }

            share.setOnClickListener{
                viewModel.share()
            }
        }
    }
}