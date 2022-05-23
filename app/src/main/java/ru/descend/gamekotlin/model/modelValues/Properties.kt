package ru.descend.gamekotlin.model.modelValues

import java.io.Serializable

class Damage(value: Int = 0, percentValue: Int = 0) : Property("Атака", value, percentValue, 2)
class Defence(value: Int = 0, percentValue: Int = 0) : Property("Защита", value, percentValue, 2)
class Health(value: Int = 0, percentValue: Int = 0) : Property("Здоровье", value, percentValue, 1)
class Speed(value: Int = 100, percentValue: Int = 0) : Property("Скорость", value, percentValue, 1)
class CritChance(value: Int = 0, percentValue: Int = 1) : Property("Шанс крит. удара", value, percentValue, 5)
class CritDamage(value: Int = 0, percentValue: Int = 150): Property("Урон крит. удара", value, percentValue, 3)
class Vampirizm(value: Int = 0, percentValue: Int = 0) : Property("Вампиризм", value, percentValue, 10)

data class Properties(
    var damage: Damage = Damage(),
    var defence: Defence = Defence(),
    var health: Health = Health(),
    var speed: Speed = Speed(),
    var critChance: CritChance = CritChance(),
    var critDamage: CritDamage = CritDamage(),
    var vampirizm: Vampirizm = Vampirizm()
): Serializable{

    override fun toString(): String {
        return "Properties(damage='$damage', defence=$defence, health=$health, speed=$speed, critChance=$critChance, critDamage=$critDamage, vampirizm=$vampirizm)"
    }

}

abstract class Property(name: String, defaultValue: Int = 0, defaultPercent: Int = 0, defaultPrice: Int = 1) : Serializable{
    var propertyName: String = name
    var price: Int = defaultPrice
    var value: Int? = defaultValue
    var percentValue: Int? = defaultPercent

    /**
     * Очистка всех значений характеристики
     */
    fun clear(){
        value = null
        percentValue = null
    }

    /**
     * Наличие данной характеристики
     */
    fun isExists(): Boolean {
        return !(value == null && percentValue == null)
    }

    override fun toString(): String {
        return "Property(propertyName='$propertyName', price=$price, value=$value, percentValue=$percentValue)"
    }
}

enum class EnumProperties(val nameProp: String): Serializable {
    DAMAGE("damage"),
    DEFENCE("defence"),
    HEALTH("health"),
    SPEED("speed"),
    CRITCHANCE("critChance"),
    CRITDAMAGE("critDamage"),
    VAMPIRIZM("vampirizm")
}