package ru.descend.gamekotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.adapters.AdapterHeroValues
import ru.descend.gamekotlin.databinding.FragmentHeroBinding
import ru.descend.gamekotlin.main.MainViewModel.Companion.hero

class FragmentHero : AbstractFragment("Информация о герое") {

    private val binding: FragmentHeroBinding by viewBinding()

    private var adapterMoney = AdapterHeroValues()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initExe()
        initListeners()
    }

    private fun init() = binding.apply {
        recyclerValues.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerValues.adapter = adapterMoney
        adapterMoney.onNewData(hero.inventory.itemsMoney)
    }

    private fun initExe() = binding.apply {

    }

    private fun initListeners() = binding.apply {

    }
}