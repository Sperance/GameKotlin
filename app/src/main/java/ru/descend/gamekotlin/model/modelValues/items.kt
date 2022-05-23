package ru.descend.gamekotlin.model.modelValues

import androidx.annotation.DrawableRes
import java.io.Serializable

enum class ItemCategory(val nameType: String) : Serializable {
    CHEST("Сундук"),
    EQUIPABLE("Вещь"),
    VALUES("Значение")
}

enum class EnumRarityItems(val text: String, val modPrice: Int) : Serializable {
    COMMON("Обычный", 1),
    UNCOMMON("Необычный", 2),
    RARE("Редкий", 4),
    EPIC("Эпический", 8),
    LEGENDARY("Легендарный", 15),
    MYTHIC("Мифический", 30),
    GOD("Божественный", 50)
}

abstract class Item(var name: String, @DrawableRes val icon: Int, val category: ItemCategory) : Serializable {
    var count: Long = 0
    var price: Long = 1
    var description: String? = null

    @DrawableRes
    abstract fun getClassIcon(): Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (category != other.category) return false
        if (count != other.count) return false
        if (price != other.price) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon
        result = 31 * result + category.hashCode()
        result = 31 * result + count.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}