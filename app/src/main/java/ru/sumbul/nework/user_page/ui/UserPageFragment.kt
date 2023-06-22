package ru.sumbul.nework.user_page.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import ru.sumbul.nework.R
import ru.sumbul.nework.auth.data.AppAuth
import ru.sumbul.nework.auth.ui.AuthViewModel
import ru.sumbul.nework.databinding.FragmentUserPageBinding
import ru.sumbul.nework.events.ui.EventFragment.Companion.textArg
import ru.sumbul.nework.events.ui.EventsViewModel
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.posts.ui.PostOnInteractionListener
import ru.sumbul.nework.posts.ui.PostViewModel
import ru.sumbul.nework.posts.ui.PostsAdapter
import ru.sumbul.nework.user_page.domain.model.WallPosts
import ru.sumbul.nework.user_page.ui.adapter.JobAdapter
import ru.sumbul.nework.user_page.ui.adapter.JobOnInteractionListener
import ru.sumbul.nework.user_page.ui.adapter.WallPostsAdapter
import ru.sumbul.nework.user_page.ui.adapter.WallPostsOnInteractionListener
import ru.sumbul.nework.util.StringArg
import ru.sumbul.nework.util.load
import javax.inject.Inject

@AndroidEntryPoint
class UserPageFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }



    @Inject
    lateinit var appAuth: AppAuth

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: UserPageViewModel by activityViewModels()
    val authViewModel: AuthViewModel by viewModels()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val postViewModel: PostViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val jobAdapter by lazy(LazyThreadSafetyMode.NONE) {
        JobAdapter(object : JobOnInteractionListener {
            override fun onClick(job: ru.sumbul.nework.user_page.domain.model.Job) {
                findNavController().navigate(
                    R.id.action_postsListFragment_to_postFragment,
                    Bundle().apply {
                        textArg = job.id.toString()
                    })
            }
            override fun onRemove(job: ru.sumbul.nework.user_page.domain.model.Job) {
                viewModel.removeJobById(job.id.toLong())
            }

            override fun onEdit(job: ru.sumbul.nework.user_page.domain.model.Job) {
                //  postViewModel.edit(post)
//                findNavController().navigate(
//                    R.id.action_userPageFragment_to_newPostFragment,
//                    Bundle().apply {
//                        textArg = job.content
//                    }
//                )
            }
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val postAdapter by lazy(LazyThreadSafetyMode.NONE) {
        WallPostsAdapter(object : WallPostsOnInteractionListener {
            override fun onClick(post: WallPosts) {
                findNavController().navigate(
                    R.id.action_userPageFragment_to_postFragment,
                    Bundle().apply {
                        textArg = post.id.toString()
                    })
            }
            override fun onEdit(post: WallPosts) {
              //  postViewModel.edit(post)
                findNavController().navigate(
                    R.id.action_userPageFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: WallPosts) {
                if (authViewModel.authorized) {
                    postViewModel.likeById(post.id.toLong())
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

            override fun onDeleteLike(post: WallPosts) {
                postViewModel.unlikeById(post.id.toLong())
            }

            override fun onRemove(post: WallPosts) {
                postViewModel.removeById(post.id.toLong())
            }
        })
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUserPageBinding.inflate(inflater, container, false)

        binding.jobList.adapter = jobAdapter
        binding.wallList.adapter = postAdapter

        val userId = arguments?.textArg

        if (arguments != null) {

            userId?.toLong()?.let { it ->
                viewModel.getUserById(it)
                viewModel.userLiveDataTransformed()?.observe(viewLifecycleOwner) { user ->
                    //  arguments?.textArg?.toLong()?.let { viewModel.getUserById(it) }
                    binding.id.text = user.id.toString()
                    user.avatar?.let { binding.avatar.load(it) }
                    binding.name.text = user.name
                }

            }

            userId?.toLong()?.let {
                viewModel.getJobs(it)
                viewModel.getJobs()?.observe(viewLifecycleOwner) { job ->
                    jobAdapter.submitList(job)
                }
            }

            userId?.toLong()?.let {
                viewModel.getWall(it)
                viewModel.getWall()?.observe(viewLifecycleOwner) { posts ->
                    postAdapter.submitList(posts)
                }
            }
        } else {
            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authorized) {
                    binding.addJob.visibility = View.VISIBLE
                    val id = appAuth.state.value?.id?.toLong()

                    if (id != null) {
                        viewModel.getUserById(id)
                    }
                    viewModel.userLiveDataTransformed()?.observe(viewLifecycleOwner) { user ->
                        //  arguments?.textArg?.toLong()?.let { viewModel.getUserById(it) }
                        binding.id.text = user.id.toString()
                        user.avatar?.let { binding.avatar.load(user.avatar) }
                        Glide.with(this)
                            .load(user.avatar)
                            .timeout(10_000)
                            .centerCrop()
                            .error(R.drawable.ic_baseline_terrain_24)
                            .into(binding.avatar)
                        binding.name.text = user.name
                    }

                    viewModel.getMyJobs()
                    viewModel.getMyJobsLiveData()?.observe(viewLifecycleOwner) { job ->
                        jobAdapter.submitList(job)
                    }

                    viewModel.getMyWall()
                    viewModel.getMyWallLiveData()?.observe(viewLifecycleOwner) { posts ->
                        postAdapter.submitList(posts)
                    }

                    binding.addJob.setOnClickListener {
                        findNavController().navigate(R.id.action_userPageFragment_to_newJobFragment)
                    }

                    binding.addPost.setOnClickListener {
                        findNavController().navigate(R.id.action_userPageFragment_to_newPostFragment)
                    }

                } else {
                    val myDialogFragment = NoProfile()
                    val manager = childFragmentManager
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    myDialogFragment.show(transaction, "dialog")
                }
            }
        }


        return binding.root
    }

}