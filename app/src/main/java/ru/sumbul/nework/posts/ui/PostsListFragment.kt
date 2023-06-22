package ru.sumbul.nework.posts.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import ru.sumbul.nework.auth.data.AppAuth
import ru.sumbul.nework.auth.ui.AuthViewModel
import ru.sumbul.nework.databinding.FragmentPostsListBinding
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.events.ui.EventFragment.Companion.textArg
import ru.sumbul.nework.events.ui.adapter.EventsAdapter
import ru.sumbul.nework.events.ui.adapter.OnInteractionListener
import ru.sumbul.nework.posts.domain.model.PostResponse
import javax.inject.Inject


@AndroidEntryPoint
class PostsListFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: PostViewModel by activityViewModels()
    val authViewModel: AuthViewModel by viewModels()

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

            override fun onEdit(post: PostResponse) {
               viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_postsListFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: PostResponse) {
                if (authViewModel.authorized) {
                    viewModel.likeById(post.id.toLong())
                } else {
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(
                            it1,
                            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                        )
                            .setMessage(resources.getString(R.string.alert_dialog))
                            .setNeutralButton(resources.getString(R.string.sign_in_button)) { _, _ ->
                                findNavController().navigate(R.id.action_postsListFragment_to_signInFragment)
                            }
                            .show()
                    }
                }
            }

            override fun onDeleteLike(post: PostResponse) {
                viewModel.unlikeById(post.id.toLong())
            }

            override fun onRemove(post: PostResponse) {
                viewModel.removeById(post.id.toLong())
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


        var menuProvider: MenuProvider? = null
        authViewModel.data.observe(viewLifecycleOwner) {

            binding.fab.setOnClickListener {
                if (authViewModel.authorized) {
                    findNavController().navigate(R.id.action_postsListFragment_to_newPostFragment)
                } else {
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(
                            it1,
                            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                        )
                            .setMessage(resources.getString(R.string.alert_dialog))
                            .setNeutralButton(resources.getString(R.string.sign_in_button)) { _, _ ->
                                findNavController().navigate(R.id.action_postsListFragment_to_signInFragment)
                            }
                            .setNegativeButton(resources.getString(R.string.sign_up)) { _, _ ->
                                findNavController().navigate(R.id.action_postsListFragment_to_signUpFragment)
                            }
                            .show()
                    }
                }
            }

            menuProvider?.let(requireActivity()::removeMenuProvider)

            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_auth, menu)

                    menu.setGroupVisible(R.id.authorized, authViewModel.authorized)
                    menu.setGroupVisible(R.id.unauthorized, !authViewModel.authorized)

                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.logout -> {
                            appAuth.removeAuth()
                            true
                        }
                        R.id.signIn -> {
                            findNavController().navigate(R.id.action_postsListFragment_to_signInFragment)
                            true
                        }
                        R.id.signUp -> {
                            findNavController().navigate(R.id.action_postsListFragment_to_signUpFragment)
                            true
                        }
                        else -> false
                    }
            }.apply {
                menuProvider = this
            }, viewLifecycleOwner)
        }

        return binding.root
    }

}