package com.example.plantgrowingapp.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.plantgrowingapp.databinding.PlantItemBinding
import com.example.plantgrowingapp.local.domain.PlantDomain

class PlantAdapter(
    private val onClickListener: OnClickListener,
    private val onButtonClickListener: OnButtonClickListener
) : ListAdapter<PlantDomain, PlantAdapter.PlantViewHolder>(DiffCallback) {

    class PlantViewHolder private constructor(
        val binding: PlantItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, buttonClickListener: OnButtonClickListener, item: PlantDomain) {
            binding.plantDomain = item
            binding.plantCallback = clickListener
            binding.plantButtonCallBack = buttonClickListener
            binding.executePendingBindings()
        }

        //With a companion object we can get a function or a property to be tied to a class rather than to instances of it
        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlantItemBinding.inflate(layoutInflater, parent, false)

                PlantViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        return PlantViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        return holder.bind(onClickListener, onButtonClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [plant]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<PlantDomain>() {
        override fun areItemsTheSame(oldItem: PlantDomain, newItem: PlantDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PlantDomain, newItem: PlantDomain): Boolean {
            return oldItem.plantId == newItem.plantId
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [plant]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [plant]
     */
    class OnClickListener(val clickListener: (plant: PlantDomain) -> Unit) {
        fun onClick(plant: PlantDomain) = clickListener(plant)
    }

    class OnButtonClickListener(val clickListener: (plant: PlantDomain) -> Unit) {
        fun onClick(plant: PlantDomain) = clickListener(plant)
    }
}