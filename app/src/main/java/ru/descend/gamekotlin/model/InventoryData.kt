package ru.descend.gamekotlin.model

import ru.descend.dellib.extensions.log
import ru.descend.gamekotlin.model.modelValues.Hero
import ru.descend.gamekotlin.model.modelValues.EquipItem
import ru.descend.gamekotlin.model.modelValues.Item
import ru.descend.gamekotlin.model.modelValues.MoneyValues
import java.io.Serializable

class InventoryData(private val hero: Hero) : Serializable {

    /**
     * Список объектов денег персонажа
     */
    val itemsMoney = MoneyValues()

    /**
     * Список объектов вещей персонажа
     */
    val items = ArrayList<Item>()

    /**
     * Проверка на наличие вещи в инвентаре
     * @param item Вещь для поиска в инвентаре
     * @return Находится ли указанная вещь в инвентаре
     */
    fun contains(item: Item): Boolean {
        return items.contains(item)
    }

    /**
     * Добавление предмета в инвентарь
     */
    fun addItem(item: Item) {
        log("Предмет ${item.name} добавлен в инвентарь ${hero.characterName} в кол-ве ${item.count} шт.")
        items.add(item)
    }

    /**
     * Удаление предмета из инвентаря
     * @return Успешно ли удалился предмет из инвентаря
     */
    fun removeItem(item: Item) : Boolean {
        log("Предмет ${item.name} удален из инвентаря ${hero.characterName}")
        return items.remove(item)
    }

    /**
     * Продажа предмета
     * @return Успешно ли продался предмет из инвентаря
     */
    fun sellItem(item: EquipItem): Boolean {
        if (!contains(item)){
            return false
        }
        val sellValue = item.price * item.rarity.modPrice
        log("Предмет ${item.name} продан из инвентаря ${hero.characterName} за $sellValue")
        itemsMoney.money += sellValue
        removeItem(item)
        return true
    }
}