package ru.descend.gamekotlin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.descend.gamekotlin.databinding.RecyclerItemValuesBinding
import ru.descend.gamekotlin.model.modelValues.ItemMoney
import ru.descend.gamekotlin.model.modelValues.MoneyValues
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

class AdapterHeroValues : RecyclerView.Adapter<AdapterHeroValues.ViewHolder>() {

    private val adapterList: ArrayList<ItemMoney> = ArrayList()

    inner class ViewHolder(val binding: RecyclerItemValuesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            RecyclerItemValuesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = adapterList[position]
        holder.binding.apply {
            textName.text = "${current.name} :"
            textValue.text = current.count.toString()
            image.load(current.icon)
        }
    }

    override fun getItemCount() = adapterList.size

    fun onNewData(newData: MoneyValues) {
        val list = ArrayList<ItemMoney>()
        newData::class.java.kotlin.declaredMemberProperties.forEach {
            list.add(it.getter.call(newData) as ItemMoney)
        }
        val diffResult = DiffUtil.calculateDiff(MyDiffUtilCallback(list, adapterList))
        diffResult.dispatchUpdatesTo(this)
        adapterList.clear()
        adapterList.addAll(list)
    }

    class MyDiffUtilCallback(
        private val newList: ArrayList<ItemMoney>,
        private val oldList: ArrayList<ItemMoney>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            newList[newItemPosition] == oldList[oldItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            newList[newItemPosition].hashCode() == oldList[oldItemPosition].hashCode()
    }
}

