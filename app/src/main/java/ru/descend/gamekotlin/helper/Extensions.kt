package ru.descend.gamekotlin.helper

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.descend.gamekotlin.main.MainActivity

val Fragment.MainActivity: MainActivity
    get() = (requireActivity() as MainActivity)

val Fragment.snackView: BottomNavigationView
    get() = MainActivity.bottomDrawer()