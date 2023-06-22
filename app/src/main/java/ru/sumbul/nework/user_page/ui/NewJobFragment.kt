package ru.sumbul.nework.user_page.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.nework.R
import ru.sumbul.nework.auth.data.AppAuth
import ru.sumbul.nework.auth.ui.AuthViewModel
import ru.sumbul.nework.databinding.FragmentNewJobBinding
import ru.sumbul.nework.user_page.domain.model.Job
import javax.inject.Inject

class NewJobFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: UserPageViewModel by activityViewModels()
    val authViewModel: AuthViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewJobBinding.inflate(inflater, container, false)

        binding.save.setOnClickListener {
            val id = binding.num.text.toString().toInt()
            val start = binding.start.text.toString()
            val finish = binding.finish.text.toString()
            val name = binding.name.text.toString()
            val position = binding.position.text.toString()
            val link = binding.link.text.toString()

            viewModel.saveJob(Job(id, name, position, start, finish, link))

            findNavController().navigateUp()
        }

        return binding.root
    }
}