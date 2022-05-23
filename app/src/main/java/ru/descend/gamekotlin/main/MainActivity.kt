package ru.descend.gamekotlin.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    fun bottomDrawer() = binding.navView

    private fun initNavigation() = binding.apply {
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.fragmentMain,
            R.id.fragmentAuthorization,
            R.id.fragmentHero,
            R.id.fragmentInventory
        ))

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            log("Открытие фрагмента: ${destination.displayName.substringAfterLast('/')}(${destination.label})")
//        }

        NavigationUI.setupWithNavController(navView, navController)
//        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController, appBarConfiguration)
    }
}