package ru.descend.gamekotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.databinding.FragmentShopsBinding

class FragmentShops : AbstractFragment("Магазины") {

    private val binding: FragmentShopsBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shops, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initExe()
        initListeners()
    }

    private fun init() = binding.apply {

    }

    private fun initExe() = binding.apply {

    }

    private fun initListeners() = binding.apply {

    }
}