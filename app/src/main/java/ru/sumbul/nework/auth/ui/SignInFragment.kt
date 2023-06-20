package ru.sumbul.nework.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentSignInBinding

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.sighInButton.setOnClickListener {
            viewModel.signInUser(
                binding.usedLogin.editText?.text.toString(),
                binding.usedPassword.editText?.text.toString()
            )
        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (viewModel.authorized) {
                findNavController().navigateUp()
            }
        }


        return binding.root
    }
}