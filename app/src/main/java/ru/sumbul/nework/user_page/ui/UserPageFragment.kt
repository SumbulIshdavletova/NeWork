package ru.sumbul.nework.user_page.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentUserPageBinding
import ru.sumbul.nework.events.ui.EventFragment.Companion.textArg
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

@AndroidEntryPoint
class UserPageFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: UserPageViewModel by activityViewModels()

    private val jobAdapter by lazy(LazyThreadSafetyMode.NONE) {
        JobAdapter(object : JobOnInteractionListener {
            override fun onClick(job: ru.sumbul.nework.user_page.domain.model.Job) {
                findNavController().navigate(
                    R.id.action_postsListFragment_to_postFragment,
                    Bundle().apply {
                        textArg = job.id.toString()
                    })
            }
        })
    }

    private val postAdapter by lazy(LazyThreadSafetyMode.NONE) {
        WallPostsAdapter(object : WallPostsOnInteractionListener {
            override fun onClick(post: WallPosts) {
                findNavController().navigate(
                    R.id.action_postsListFragment_to_postFragment,
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

        val binding = FragmentUserPageBinding.inflate(inflater, container, false)

        binding.jobList.adapter = jobAdapter
        binding.wallList.adapter = postAdapter

        val userId = arguments?.textArg

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


        return binding.root
    }


}