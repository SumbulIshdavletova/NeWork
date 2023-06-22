package ru.sumbul.nework.posts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentEventBinding
import ru.sumbul.nework.databinding.FragmentPostBinding
import ru.sumbul.nework.events.ui.EventFragment.Companion.textArg
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(inflater, container, false)


        lifecycleScope.launch {
            arguments?.textArg?.toLong()?.let { viewModel.getEventById(it) }
            viewModel.postLiveDataTransformed()?.observe(viewLifecycleOwner) { post ->
                post.authorAvatar?.let { binding.avatar.load(it) }
                post.attachment?.url?.let { binding.attachment.load(it) }
                //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

                binding.author.text = post.author
                binding.published.text = post.published
                binding.content.text = post.content

                if (post.likedByMe) {
                    binding.like.setOnClickListener {
                        viewModel.unlikeById(post.id.toLong())
                    }
                } else {
                    binding.like.setOnClickListener {
                        viewModel.likeById(post.id.toLong())
                    }
                }

                binding.author.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_postFragment_to_userPageFragment,
                        Bundle().apply {
                            textArg = post.id.toString()
                        })
                }

                binding.menu.isVisible = post.ownedByMe
                binding.menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    findNavController().navigateUp()
                                    viewModel.removeById(post.id.toLong())
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(R.id.action_postFragment_to_newPostFragment,
                                        Bundle().apply {
                                            textArg = post.content
                                        })
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }

            }
        }

        return binding.root
    }

}