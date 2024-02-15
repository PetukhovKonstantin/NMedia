package ru.netology.nmedia.activities

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.PostActionListener
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.viewmodels.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : PostActionListener {
            override fun onLike(post: Post) = viewModel.likeById(post.id)
            override fun onShare(post: Post) = viewModel.shareById(post.id)
            override fun onRemove(post: Post) = viewModel.removeById(post.id)
            override fun onEdit(post: Post) = viewModel.edit(post)
        })

        viewModel.data.observe(this) { posts ->
            val isNewPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (isNewPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.list.adapter = adapter

        viewModel.edited.observe(this) {post ->
            if (post.id != 0L) {
                binding.group3.visibility = View.VISIBLE
                binding.editPreviewContent.setText(post.content)
                binding.editContent.setText(post.content)
                binding.editContent.focusAndShowKeyboard()
            }
        }

        binding.savePost.setOnClickListener {
            val text = binding.editContent.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.saveAndChangeContent(text)
            binding.group3.visibility = View.GONE
            binding.editContent.setText("")
            binding.editContent.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }

        binding.cancelEditContent.setOnClickListener {
            binding.group3.visibility = View.GONE
            binding.editContent.setText("")
            binding.editContent.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }
    }
}