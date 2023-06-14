package ru.sumbul.nework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val navController = this.findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView= findViewById(R.id.bottom_navigation)
        navView.setupWithNavController(navController)



    }

}