package ru.sumbul.nework.auth.ui

import android.app.Activity
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentSignUpBinding

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        viewModel.data.observe(viewLifecycleOwner) {
            if (viewModel.authorized) {
                findNavController().navigateUp()
            }
        }

        val contract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val resultCode = result.resultCode
                val data = result.data

                when (resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(data),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val fileUri = data?.data

                        viewModel.changePhoto(fileUri, fileUri?.toFile())
                    }
                }
            }

        binding.addAvatar.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(500)
                .createIntent(contract::launch)
        }

        binding.sighUpButton.setOnClickListener {
            viewModel.photo.value?.file?.let { it ->
                viewModel.signUpUser(
                    binding.userLogin.text.toString(),
                    binding.userPassword.text.toString(),
                    binding.userPasswordConfirm.text.toString(),
                    it
                )
            }
        }

        return binding.root
    }

}