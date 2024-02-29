package ru.netology.nmedia.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
            viewModel.saveDraft("")
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            viewModel.saveDraft(binding.editContent.text.toString())
            findNavController().navigateUp()
        }
        return binding.root
    }
}