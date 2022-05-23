package ru.descend.gamekotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.GridLayoutManager
import ru.descend.dellib.extensions.addDecor
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.adapters.AdapterHeroInventory
import ru.descend.gamekotlin.databinding.FragmentInventoryBinding
import ru.descend.gamekotlin.main.MainViewModel.Companion.hero

class FragmentInventory : AbstractFragment("Инвентарь") {

    private val binding: FragmentInventoryBinding by viewBinding()
    private val adapterInventory = AdapterHeroInventory()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initExe()
        initListeners()
    }

    private fun init() = binding.apply {
        recyclerInventory.layoutManager = GridLayoutManager(requireContext(), 5)
        recyclerInventory.adapter = adapterInventory
    }

    private fun initExe() = binding.apply {
        adapterInventory.onNewData(hero.inventory)
    }

    private fun initListeners() = binding.apply {

    }
}