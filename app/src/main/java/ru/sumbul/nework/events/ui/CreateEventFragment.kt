package ru.sumbul.nework.events.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.FragmentCreateEventBinding

class CreateEventFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreateEventBinding.inflate(inflater, container, false)


        return binding.root
    }
}