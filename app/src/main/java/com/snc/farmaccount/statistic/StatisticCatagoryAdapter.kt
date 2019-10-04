package com.snc.farmaccount.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.SumEvent
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

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(event: SumEvent) {
            binding.sum = event
            binding.imageTag.setImageResource(event.tagImage)
            binding.titleTag.text = event.tagType
            binding.textPrice.text = "$"+event.totalPrice


            if (event.status) {
                binding.price.setBackgroundResource(R.drawable.radius_border_green)
                binding.titleTag.setTextColor(Color.parseColor("#82B763"))
            } else {
                binding.price.setBackgroundResource(R.drawable.radius_border_red)
                binding.titleTag.setTextColor(Color.parseColor("#BC4141"))
            }

            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event: SumEvent) -> Unit) {
        fun onClick(event: SumEvent) = clickListener(event)
    }
}