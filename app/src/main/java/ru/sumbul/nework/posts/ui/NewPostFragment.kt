package ru.sumbul.nework.posts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentNewPostBinding

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)



        return binding.root
    }

}