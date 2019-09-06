package com.snc.farmaccount.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.databinding.ItemStatisticEventBinding

class StatisticEventAdapter(var onClickListener: OnClickListener,var viewModel: StatisticViewModel):
    ListAdapter<Event, StatisticEventAdapter.EventViewHolder>(DiffCallback){

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder (
            ItemStatisticEventBinding
                .inflate(LayoutInflater.from(parent.context ), parent, false))

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: Event = getItem(position)


        holder.itemView.setOnClickListener {
            selectedPosition = position
            onClickListener.onClick(event)
            notifyDataSetChanged()

        }
        holder.bind(event,selectedPosition)
    }


    class EventViewHolder(private var binding: ItemStatisticEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, selectedPosition: Int) {
            binding.event = event
            if (event.tag == "早餐") {
                binding.imageTag.setImageResource(R.drawable.tag_breakfast)
            }
            if (event.tag == "午餐") {
                binding.imageTag.setImageResource(R.drawable.tag_lunch)
            }
            if (event.tag == "晚餐") {
                binding.imageTag.setImageResource(R.drawable.tag_dinner)
            }
            if (event.tag == "點心") {
                binding.imageTag.setImageResource(R.drawable.tag_dessert)
            }
            if (event.tag == "衣服") {
                binding.imageTag.setImageResource(R.drawable.tag_cloth)
            }
            if (event.tag == "住") {
                binding.imageTag.setImageResource(R.drawable.tag_live)
            }
            if (event.tag == "交通") {
                binding.imageTag.setImageResource(R.drawable.tag_traffic)
            }
            if (event.tag == "娛樂") {
                binding.imageTag.setImageResource(R.drawable.tag_fun)
            }
            if (event.tag == "薪水") {
                binding.imageTag.setImageResource(R.drawable.tag_payment)
            }
            if (event.tag == "中獎") {
                binding.imageTag.setImageResource(R.drawable.tag_lottery)
            }

            binding.titleTag.text = event.tag
            binding.textPrice.text = event.price

            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

}