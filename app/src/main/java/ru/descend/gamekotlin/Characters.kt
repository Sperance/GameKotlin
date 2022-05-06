package ru.descend.gamekotlin

import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class Warrior(name: String, health: Int) : Character(name, health) {

    var helmet: Helmet? = null
        private set

    var weapon: Weapon? = null
        private set

    var body: Body? = null
        private set

    fun equipItem(item: EquipItem) {
        val fieldName = item.javaClass.kotlin.simpleName?.firstLower()
        val declaredField = this::class.java.getDeclaredField(fieldName!!)
        log("Персонаж $characterName класса ${this.javaClass.simpleName} экипировал ${item.rarity.text}} предмет ${item.nameItem}")
        log("BEFORE: ${declaredField.get(this)}")
        declaredField.set(this, item)
        log("AFTER: ${declaredField.get(this)}")
    }

    override fun getAttack() {
        TODO("Not yet implemented")
    }

    override fun getDefence() {
        TODO("Not yet implemented")
    }
}

class Enemy(name: String, health: Int) : Character(name, health){
    var stats: ArrayList<Property> = ArrayList()

    fun addItem(item: EquipItem){
        item.stats.forEach {
            val statTemp = this.stats.get(it)
            if (statTemp == null)
                this.stats.add(it)
            else
                this.stats.findStat(statTemp)?.addStat(it)
        }
    }

    override fun getAttack() {
        TODO("Not yet implemented")
    }

    override fun getDefence() {
        TODO("Not yet implemented")
    }
}

private fun <T> Collection<T>.get(element: T) = find { it == element }
private fun Collection<Property>.findStat(element: Property) = find { it.propertyName == element.propertyName }
public fun Collection<Property>.printStat() = joinToString { it.propertyName + " : " + it.value + " % " + it.percentValue }

abstract class Character(name: String, health: Int) {
    var characterLevel: Int = 1
    var characterName: String = name
    var characterHealth: Int = health
    var characterMaxHealth: Int = health
        private set

    fun setMaxHealth(value: Int) {
        log("Изменение максимального кол-ва жизней у $characterName с $characterMaxHealth на $value")
        characterMaxHealth = value
    }

    abstract fun getAttack()
    abstract fun getDefence()
}

enum class EnumRarityItems(val text: String, val modPrice: Int) {
    COMMON("Обычный", 1),
    UNCOMMON("Необычный", 2),
    RARE("Редкий", 4),
    EPIC("Эпический", 8),
    LEGENDARY("Легендарный", 15),
    MYTHIC("Мифический", 30),
    GOD("Божественный", 50)
}

abstract class EquipItem(name: String) {
    var stats: ArrayList<Property> = ArrayList()
    var nameItem: String = name
    var rarity: EnumRarityItems = EnumRarityItems.COMMON

    fun changeStats(body: (ArrayList<Property>) -> Boolean){
        body.invoke(stats)
    }

    /**
     * Получение стоимости предмета
     */
    fun getPrice(): Int {
        var result = 0
        stats.forEach {
            result += it.price
        }
        return result * rarity.modPrice
    }
}

class Helmet(name: String) : EquipItem(name)
class Weapon(name: String) : EquipItem(name)
class Body(name: String) : EquipItem(name)