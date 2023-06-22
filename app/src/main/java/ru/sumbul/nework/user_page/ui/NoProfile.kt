package ru.sumbul.nework.user_page.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import ru.sumbul.nework.R
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoProfile : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("You don't have a profile yet")
                .setMessage("Sing in or sign up")
                .setCancelable(true)
                .setPositiveButton("Sign in") { _, _ ->
                    findNavController().navigate(R.id.action_userPageFragment_to_signInFragment)
                }
                .setNegativeButton(
                    "Sign up"
                ) { _, _ ->
                    findNavController().navigate(R.id.action_userPageFragment_to_signUpFragment)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}