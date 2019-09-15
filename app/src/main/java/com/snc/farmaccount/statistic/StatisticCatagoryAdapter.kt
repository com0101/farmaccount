package com.snc.farmaccount.statistic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.internal.view.SupportSubMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.SumEvent
import com.snc.farmaccount.databinding.ItemStatisticEventBinding
import com.snc.farmaccount.databinding.ItemStatisticTotalBinding

class StatisticCatagoryAdapter(var onClickListener: OnClickListener, var viewModel: StatisticViewModel):
    ListAdapter<SumEvent, StatisticCatagoryAdapter.EventViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder (
            ItemStatisticTotalBinding
                .inflate(LayoutInflater.from(parent.context ), parent, false))

    }

    companion object DiffCallback : DiffUtil.ItemCallback<SumEvent>() {
        override fun areItemsTheSame(oldItem: SumEvent, newItem: SumEvent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SumEvent, newItem: SumEvent): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: SumEvent = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(event)
            notifyDataSetChanged()

        }
        holder.bind(event)
    }


    class EventViewHolder(private var binding: ItemStatisticTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(event: SumEvent) {
            binding.sum = event
            binding.imageTag.setImageResource(event.tagImage)
            binding.titleTag.text = event.tagType
            binding.textPrice.text = event.totalPrice
            if (event.status) {
                binding.price.setBackgroundResource(R.drawable.greenbar)
                binding.titleTag.setTextColor(R.color.dark_green)
            } else {
                binding.price.setBackgroundResource(R.drawable.redbar)
                binding.titleTag.setTextColor(R.color.dark_red)
            }

            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event: SumEvent) -> Unit) {
        fun onClick(event: SumEvent) = clickListener(event)
    }
}