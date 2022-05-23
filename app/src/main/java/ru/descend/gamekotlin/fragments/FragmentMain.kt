package ru.descend.gamekotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import ru.descend.dellib.elements.DesDialogAccept
import ru.descend.dellib.elements.DesSnackBar
import ru.descend.dellib.elements.EnumConfirmColors
import ru.descend.dellib.elements.SnackColors
import ru.descend.dellib.extensions.asIntNull
import ru.descend.dellib.extensions.log
import ru.descend.dellib.extensions.safetyNavigate
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.databinding.FragmentMainBinding
import ru.descend.gamekotlin.helper.*
import ru.descend.gamekotlin.main.MainViewModel.Companion.hero
import ru.descend.gamekotlin.main.MainViewModel.Companion.saveOffline
import ru.descend.gamekotlin.model.modelFirebase.HeroModel
import ru.descend.gamekotlin.model.modelValues.EnumProperties
import ru.descend.gamekotlin.model.modelValues.Weapon
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class FragmentMain : AbstractFragment("Главный") {

    private val binding: FragmentMainBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
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

        desAttack.textListener = {
            val weap = Weapon("Меч погибели")
            hero.equipItem(weap)
            hero.weapon?.setProperty(EnumProperties.DAMAGE, it.asIntNull())
//            if (MainViewModel.hero.weapon == null){
//                val weap = Weapon("Меч погибели")
//                weap.setStats { stats ->
//                    stats.add(Damage(10))
//                }
//                MainViewModel.hero.equipItem(weap)
//            }
//            MainViewModel.hero.weapon?.setStat(Damage(), it.toInt())
//            log("NEW STAT: " + MainViewModel.hero.weapon?.stats?.joinToString { prop -> prop.toString() })
        }

        buttonAddMoney.setOnClickListener {
            val addingValue = (1..10).random(Random(Date().time))
            hero.inventory.itemsMoney.money += addingValue
            DesSnackBar(snackView, "Добавлено: $addingValue монет. Всего: ${hero.inventory.itemsMoney.money.count} монет")
        }
        buttonInfo.setOnClickListener { 
            log("Информация:\n Оружие: ${hero.weapon.toString()}\n Тело: ${hero.body.toString()}\n Шлем: ${hero.helmet.toString()}")
        }
        buttonBack.setOnClickListener {
            DesDialogAccept("Перейти на следующий фрагмент? Вы точно этого хотите?", "Перейти", EnumConfirmColors.RED) {
                DesSnackBar(snackView, "Перейдено на следующий фрагмент", SnackColors.ACCESS)
            }
        }
        buttonAddItem.setOnClickListener {
            hero.addItem(Weapon("Тестовый меч"))
        }
        buttonShops.setOnClickListener {
            safetyNavigate(FragmentMainDirections.actionFragmentMainToFragmentShops())
        }
        buttonSave.setOnClickListener {
            saveOffline()
        }
    }

    private fun initListeners() = binding.apply {

    }
}