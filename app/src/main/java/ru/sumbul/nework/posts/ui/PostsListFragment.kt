package ru.sumbul.nework.posts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentPostsListBinding
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.ui.EventFragment.Companion.textArg
import ru.sumbul.nework.events.ui.adapter.EventsAdapter
import ru.sumbul.nework.events.ui.adapter.OnInteractionListener
import ru.sumbul.nework.posts.domain.model.PostResponse


@AndroidEntryPoint
class PostsListFragment : Fragment() {


    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: PostViewModel by activityViewModels()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostsAdapter(object : PostOnInteractionListener {
            override fun onClick(post: PostResponse) {
                findNavController().navigate(
                    R.id.action_postsListFragment_to_postFragment,
                    Bundle().apply {
                        textArg = post.id.toString()
                    })
            }

            override fun onAuthor(post: PostResponse) {
                findNavController().navigate(
                    R.id.action_postsListFragment_to_userPageFragment,
                    Bundle().apply {
                        textArg = post.id.toString()
                    })
            }
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsListBinding.inflate(inflater, container, false)

        binding.list.adapter = adapter
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
        }


        binding.swiperefresh.setOnRefreshListener(adapter::refresh)

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swiperefresh.isRefreshing =
                    state.refresh is LoadState.Loading
//                            state.prepend is LoadState.Loading ||
//                            state.append is LoadState.Loading
            }
        }


        binding.fab.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(
                    it1,
                    R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                )
                    .setMessage(resources.getString(R.string.alert_dialog))
//                            .setNeutralButton(resources.getString(R.string.sign_in_button)) { _, _ ->
//                                findNavController().navigate(R.id.action_feedFragment_to_singInFragment)
//                            }
                    .show()
            }
        }


        return binding.root
    }

}