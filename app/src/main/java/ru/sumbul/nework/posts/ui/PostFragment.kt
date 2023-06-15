package ru.sumbul.nework.posts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentEventBinding
import ru.sumbul.nework.databinding.FragmentPostBinding
import ru.sumbul.nework.events.ui.EventsViewModel
import ru.sumbul.nework.util.StringArg
import ru.sumbul.nework.util.load


class PostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: PostViewModel by activityViewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(inflater, container,false)

        lifecycleScope.launch {
            arguments?.textArg?.toLong()?.let { viewModel.getEventById(it) }
            viewModel.postLiveDataTransformed()?.observe(viewLifecycleOwner) { post ->
                post.authorAvatar?.let { binding.avatar.load(it) }
                post.attachment?.url?.let { binding.attachment.load(it) }
                //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

                binding.author.text = post.author
                binding.published.text = post.published
                binding.content.text = post.content


                binding.like.setOnClickListener {
                //    viewModel.likeById(event.id.toLong())
                }

            }
        }

        return binding.root
    }

}