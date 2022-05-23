package ru.descend.gamekotlin.adapters

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.descend.dellib.elements.DesSnackBar
import ru.descend.dellib.extensions.setVisibility
import ru.descend.gamekotlin.databinding.RecyclerItemInventoryBinding
import ru.descend.gamekotlin.databinding.RecyclerItemValuesBinding
import ru.descend.gamekotlin.main.MainViewModel.Companion.hero
import ru.descend.gamekotlin.model.InventoryData
import ru.descend.gamekotlin.model.modelValues.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

class AdapterHeroInventory : RecyclerView.Adapter<AdapterHeroInventory.ViewHolder>() {

    private val adapterList: ArrayList<Item> = ArrayList()

    inner class ViewHolder(val binding: RecyclerItemInventoryBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val popup = PopupMenu(binding.root.context, it)
                popup.menu.add("Информация")

                val itemValue = adapterList[adapterPosition]
                if (itemValue is EquipItem)
                    popup.menu.add("Продать")

                popup.setOnMenuItemClickListener { item ->
                    when (item.title){
                        "Информация" -> {
                            DesSnackBar(it, adapterList[adapterPosition].description?:"")
                            popup.dismiss()
                        }
                        "Продать" -> {
                            hero.inventory.sellItem(itemValue as EquipItem)
                            onNewData(hero.inventory)
                            popup.dismiss()
                        }
                    }
                    true
                }

                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            RecyclerItemInventoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = adapterList[position]
        holder.binding.apply {
            imageItem.load(current.icon)
            textCount.text = current.count.toString()
            val show = current.count == 1L && current.category != ItemCategory.VALUES
            textCount.setVisibility(!show)
        }
    }

    override fun getItemCount() = adapterList.size

    fun onNewData(newData: InventoryData) {
        val list = ArrayList<Item>()

        list.add(newData.itemsMoney.money)
        list.add(newData.itemsMoney.cristals)
        list.addAll(newData.items)

        val diffResult = DiffUtil.calculateDiff(MyDiffUtilCallback(list, adapterList))
        diffResult.dispatchUpdatesTo(this)
        adapterList.clear()
        adapterList.addAll(list)
    }

    class MyDiffUtilCallback(
        private val newList: ArrayList<Item>,
        private val oldList: ArrayList<Item>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val value = newList[newItemPosition] == oldList[oldItemPosition]
            return value
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val value = newList[newItemPosition].hashCode() == oldList[newItemPosition].hashCode()
            return value
        }
    }
}

