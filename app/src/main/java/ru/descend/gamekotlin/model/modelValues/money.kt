package ru.descend.gamekotlin.model.modelValues

import ru.descend.gamekotlin.R
import java.io.Serializable

class ItemMoney(name: String, icon: Int) : Item(name, icon, ItemCategory.VALUES), Serializable {

    override fun getClassIcon() = R.drawable.ic_baseline_attach_money

    override fun toString() = "$name: $count"

    operator fun plusAssign(value: Int) {this.count += value}
    operator fun minusAssign(value: Int){ this.count -= value }
    operator fun timesAssign(value: Int){ this.count *= value }
    operator fun divAssign(value: Int){ this.count /= value }

    operator fun plusAssign(value: Long) { this.count += value }
    operator fun minusAssign(value: Long){ this.count -= value }
    operator fun timesAssign(value: Long){ this.count *= value }
    operator fun divAssign(value: Long){ this.count /= value }
}

data class MoneyValues(
    var money: ItemMoney = ItemMoney("Монеты", R.drawable.ic_svg_money),
    var cristals: ItemMoney = ItemMoney("Кристаллы", R.drawable.ic_crystal_shard_svgrepo_com),
) : Serializable