package ru.descend.gamekotlin.model.modelValues

import androidx.annotation.DrawableRes
import ru.descend.dellib.log
import ru.descend.gamekotlin.R
import java.io.Serializable
import kotlin.reflect.full.memberProperties

class Helmet(name: String) : EquipItem(name, R.drawable.ic_svg_helmet)
class Weapon(name: String) : EquipItem(name, R.drawable.ic_svg_weapon)
class Body(name: String) : EquipItem(name, R.drawable.ic__svg_armor)

abstract class EquipItem(name: String, @DrawableRes icon: Int) : Item(name, icon, ItemCategory.EQUIPABLE), Serializable {

    var properties: Properties = Properties()
    var nameItem: String = name
    var rarity: EnumRarityItems = EnumRarityItems.COMMON

    override fun getClassIcon() = R.drawable.ic_svg_bag

    init {
        count += 1
    }

    /**
     * Очистка выбранной характеристики у предмета
     * @param propClass характеристика
     */
    fun clearProperty(propClass: EnumProperties) {
        Properties::class.java.kotlin.memberProperties.find { it.name == propClass.nameProp }?.let {
            (it.getter.call(properties) as Property).clear()
        }
    }

    /**
     * Проверка наличия указанной характеристики у предмета
     */
    fun checkProperty(propClass: EnumProperties): Boolean {
        Properties::class.java.kotlin.memberProperties.find { it.name == propClass.nameProp }?.let {
            return (it.getter.call(properties) as Property).isExists()
        } ?: return false
    }

    /**
     * Установка характеристики предмету
     * @param propClass характеристика
     * @param value значение характеристики
     * @param percent процентное увеличение значения характеристики
     */
    fun setProperty(propClass: EnumProperties, value: Int? = null, percent: Int? = null) {
        if (value == null && percent == null) return
        Properties::class.java.kotlin.memberProperties.find { it.name == propClass.nameProp }?.let {
            if (value != null) (it.getter.call(properties) as Property).value = value
            if (percent != null) (it.getter.call(properties) as Property).percentValue = percent
            log(
                "Характеристика изменена: ${it.name} значение: ${(it.getter.call(properties) as Property).value} ${
                    (it.getter.call(
                        properties
                    ) as Property).percentValue
                }%"
            )
        }
    }

    /**
     * Установка характеристики предмету
     * @param propClass характеристика
     * @param value значение характеристики
     * @param percent процентное увеличение значения характеристики
     */
    fun addProperty(propClass: EnumProperties, value: Int? = null, percent: Int? = null) {
        if (value == null && percent == null) return
        Properties::class.java.kotlin.memberProperties.find { it.name == propClass.nameProp }?.let {
            if ((it.getter.call(properties) as Property).isExists()) {
                if (value != null) (it.getter.call(properties) as Property).value?.plus(value)
                if (percent != null) (it.getter.call(properties) as Property).percentValue?.plus(
                    percent
                )
                log(
                    "Характеристика изменена: ${it.name} значение: ${(it.getter.call(properties) as Property).value} ${
                        (it.getter.call(
                            properties
                        ) as Property).percentValue
                    }%"
                )
            } else {
                log("Данной характеристики ${it.name} нет у предмета. Изменение невозможно")
            }
        }
    }

    override fun toString(): String {
        return "$nameItem {$properties}"
    }
}