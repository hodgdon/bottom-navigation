package com.hodgdon.nav

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hodgdon.nav.databinding.ActivityMainBinding
import com.hodgdon.nav.ui.home.NavFragment
import com.hodgdon.nav.ui.home.NavFragmentArgs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.a, R.id.b, R.id.c))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                val v = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_main)
                val fragment = v.getFragment<NavFragment>()
                val parentFragmentManager = fragment.parentFragmentManager
                val childFragmentManager = fragment.childFragmentManager


                val backQueue = navController.backQueue
                val stack = backQueue.map {
                    destinationString(it).toString().padEnd(8) + it.destination.displayName
                }.joinToString(separator = "\n")
                binding.textView.text = stack
            }
        })

    }

    private fun destinationString(backStackEntry: NavBackStackEntry?): String? {
        val destination = backStackEntry?.destination
        return if(destination is NavGraph) {
            "🕸"
        } else {backStackEntry?.arguments?.let { NavFragmentArgs.fromBundle(it) }?.destination}
    }
}