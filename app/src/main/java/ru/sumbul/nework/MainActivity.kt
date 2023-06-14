package ru.sumbul.nework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sumbul.nework.databinding.ActivityMainBinding
import ru.sumbul.nework.events.ui.EventsListFragment
import ru.sumbul.nework.posts.ui.PostsListFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // replaceFragment(EventsListFragment())
//
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.home ->
//                    //replaceFragment(PostsListFragment())
//                    R.id.events
//                -> findNavController(R.id.nav_host_fragment).navigate(
//                    R.id.
//                )
//                //replaceFragment(EventsListFragment())
//                //      R.id.profile - >
//                else -> {
//                }
//            }
//            true
//        }

    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }
}