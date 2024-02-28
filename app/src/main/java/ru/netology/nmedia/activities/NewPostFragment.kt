package ru.netology.nmedia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodels.PostViewModel

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    companion object {
        var Bundle.textArg: String? by StringArg
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.textArg?.let(binding.editContent::setText)

        binding.ok.setOnClickListener {
            viewModel.saveAndChangeContent(binding.editContent.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        val text = activity?.intent?.getStringExtra(Intent.EXTRA_TEXT)
        if (!text.isNullOrBlank()) {
            binding.editContent.setText(text)
        }

        return binding.root
    }
}
//
//object NewPostContract : ActivityResultContract<String?, String?>() {
//    override fun createIntent(context: Context, input: String?) = Intent(context, NewPostFragment::class.java).apply { putExtra(Intent.EXTRA_TEXT, input) }
//
//    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(Intent.EXTRA_TEXT)
//}