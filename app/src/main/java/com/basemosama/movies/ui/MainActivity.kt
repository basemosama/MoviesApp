package com.basemosama.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.basemosama.movies.R
import com.basemosama.movies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var  mainBinding:ActivityMainBinding
    lateinit var  appBarConfiguration:AppBarConfiguration
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNavigation()

    }

    private fun setupNavigation(){
        appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.moviesFragment,
            R.id.detailsFragment,
            R.id.exploreFragment
        )).build()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        mainBinding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(){_, destination, _ ->
            mainBinding.bottomNavigation.isVisible = destination.id != R.id.detailsFragment

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}