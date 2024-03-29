package ru.netology.nmedia.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.PostActionListener
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodels.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    companion object {
        var Bundle.textArg: String? by StringArg
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : PostActionListener {
            override fun onLike(post: Post) = viewModel.likeById(post.id)

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) = viewModel.removeById(post.id)

            override fun onEdit(post: Post) = viewModel.edit(post)

            override fun onPlayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                val videoIntent = Intent.createChooser(intent, getString(R.string.play_video))
                startActivity(videoIntent)
            }

            override fun onOpenPost(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_postFragment, Bundle().apply { textArg = post.id.toString() })
            }
        })

        val postId = if (!arguments?.textArg.isNullOrEmpty()) { arguments?.textArg?.toLongOrNull() ?: 0L } else { 0L }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val isNewPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            val filteredPosts = if (postId != 0L) { posts.filter { it.id == postId } } else { posts }
            adapter.submitList(filteredPosts) {
                if (isNewPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.list.adapter = adapter

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply { textArg = post.content })
            }
        }

        binding.addPost.setOnClickListener {
            val content = viewModel.getDraft()

            if (content.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply { textArg = content })
            }
        }

        return binding.root
    }
}