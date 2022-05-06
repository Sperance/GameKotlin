package ru.descend.gamekotlin

abstract class Property(name: String, defaultValue: Int = 0, defaultPercent: Int = 0, defaultPrice: Int = 1){
    var propertyName: String = name
    var price: Int = defaultPrice
    var value: Int = defaultValue
    var percentValue: Int = defaultPercent

    fun addStat(stat: Property){
        this.price += stat.price
        this.value += stat.value
        this.percentValue += stat.percentValue
    }
}

class Damage(value: Int = 0, percentValue: Int = 0) : Property("Атака", value, percentValue, 2)
class Defence(value: Int = 0, percentValue: Int = 0) : Property("Защита", value, percentValue, 2)
class Health(value: Int = 0, percentValue: Int = 0) : Property("Здоровье", value, percentValue, 1)
class Speed(value: Int = 100, percentValue: Int = 0) : Property("Скорость", value, percentValue, 1)
class CritChance(value: Int = 0, percentValue: Int = 1) : Property("Шанс крит. удара", value, percentValue, 5)
class CritDamage(value: Int = 0, percentValue: Int = 150): Property("Урон крит. удара", value, percentValue, 3)
class Vampirizm(value: Int = 0, percentValue: Int = 0) : Property("Вампиризм", value, percentValue, 10)