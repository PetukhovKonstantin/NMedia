package ru.netology.nmedia.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodels.PostViewModel

class PostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    companion object {
        var Bundle.textArg: String? by StringArg
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val postId = if (!arguments?.textArg.isNullOrEmpty()) { arguments?.textArg?.toLongOrNull() ?: 0L } else { 0L }

//        viewModel.data.observe(viewLifecycleOwner) { posts ->
//            val post = posts.find { it.id == postId } ?: return@observe
//            with(binding) {
//                author.text = post.author
//                published.text = post.published
//                content.text = post.content
//                like.isChecked = post.likedByMe
//                like.setText(post.likeCount.toString())
//                share.setText(PostService.ConvertCountToShortString(post.shareCount))
//                groupVideo.visibility = if (post.video.isNullOrEmpty()) View.GONE else View.VISIBLE
//
//                groupVideo.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
//                    val videoIntent = Intent.createChooser(intent, getString(R.string.play_video))
//                    startActivity(videoIntent)
//                }
//
//                like.setOnClickListener {
//                    viewModel.likeById(post.id)
//                }
//
//                share.setOnClickListener {
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, post.content)
//                        type = "text/plain"
//                    }
//                    val shareIntent =
//                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                    startActivity(shareIntent)
//                    viewModel.shareById(post.id)
//                }
//
//                menu.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate(R.menu.options_post)
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    viewModel.removeById(post.id)
//                                    findNavController().navigateUp()
//                                    true
//                                }
//
//                                R.id.edit -> {
//                                    viewModel.edit(post)
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                    }.show()
//                }
//            }
//        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                findNavController().navigate(R.id.action_postFragment_to_newPostFragment, Bundle().apply { textArg = post.content })
            }
        }

        return binding.root
    }
}