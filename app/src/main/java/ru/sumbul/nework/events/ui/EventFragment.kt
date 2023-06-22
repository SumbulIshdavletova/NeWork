package ru.sumbul.nework.events.ui

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentEventBinding
import ru.sumbul.nework.databinding.FragmentEventsListBinding
import ru.sumbul.nework.posts.ui.PostFragment.Companion.textArg
import ru.sumbul.nework.util.StringArg
import ru.sumbul.nework.util.load


class EventFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: EventsViewModel by activityViewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEventBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            arguments?.textArg?.toLong()?.let { viewModel.getEventById(it) }
            viewModel.eventLiveDataTransformed()?.observe(viewLifecycleOwner) { event ->
                event.authorAvatar?.let { binding.avatar.load(it) }
                event.attachment?.url?.let { binding.attachment.load(it) }
                //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

                binding.author.text = event.author
                binding.published.text = event.published
                binding.dateTime.text = event.datetime
                binding.content.text = event.content


                if (event.likedByMe) {
                    binding.like.setOnClickListener {
                        viewModel.unlikeById(event.id.toLong())
                    }
                } else {
                    binding.like.setOnClickListener {
                        viewModel.likeById(event.id.toLong())
                    }
                }

                binding.author.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_eventFragment_to_userPageFragment,
                        Bundle().apply {
                            textArg = event.id.toString()
                        })
                }

                binding.menu.isVisible = event.ownedByMe
                binding.menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    findNavController().navigateUp()
                                    viewModel.removeById(event.id.toLong())
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(event)
                                    findNavController().navigate(R.id.action_eventFragment_to_createEventFragment,
                                        Bundle().apply {
                                            textArg = event.content
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
