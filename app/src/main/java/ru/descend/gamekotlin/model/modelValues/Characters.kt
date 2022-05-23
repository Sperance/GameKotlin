package ru.descend.gamekotlin.model.modelValues

import ru.descend.dellib.firstLower
import ru.descend.dellib.log
import ru.descend.gamekotlin.model.InventoryData
import java.io.Serializable
import java.util.*

class Hero(name: String, health: Int) : Character(name, health), Serializable {

    /**
     * Инвентарь персонажа
     */
    var inventory = InventoryData(this)

    var helmet: Helmet? = null
        private set

    var weapon: Weapon? = null
        private set

    var body: Body? = null
        private set

    /**
     * Добавление предмета в инветарь персонажа
     * @param item предмет для добавления
     */
    fun addItem(item: Item){
        inventory.addItem(item)
    }

    /**
     * Проверка на занятость слота вещи экипировки персонажа
     */
    fun isEquippedItem(item: EquipItem): Boolean {
        val fieldName = item.javaClass.kotlin.simpleName?.firstLower()
        return this::class.java.getDeclaredField(fieldName!!).get(this) == null
    }

    /**
     * Экипировка вещи персонажа
     * @param item вещь для экипировки
     */
    fun equipItem(item: EquipItem) {
        val fieldName = item.javaClass.kotlin.simpleName?.firstLower()
        this::class.java.getDeclaredField(fieldName!!).set(this, item)
        addItem(item)
    }
}

abstract class Character(name: String, health: Int) : Serializable {
    var properties: Properties = Properties()
    var UID: String = UUID.randomUUID().toString()
    var characterLevel: Int = 1
    var characterName: String = name
    var characterHealth: Int = health
    private var characterMaxHealth: Int = health

    fun setMaxHealth(value: Int) {
        log("Изменение максимального кол-ва жизней у $characterName с $characterMaxHealth на $value")
        characterMaxHealth = value
    }
}