package com.snc.farmaccount.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R

import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.databinding.ItemAddEventBinding

class DayEventAdapter(var onClickListener: OnClickListener,var viewModel: DayViewModel):
    ListAdapter<Event, DayEventAdapter.ColorViewHolder>(DiffCallback){

    var selectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder (ItemAddEventBinding
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

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val event: Event = getItem(position)


        holder.itemView.setOnClickListener {
            selectedPosition = position
            onClickListener.onClick(event)
            notifyDataSetChanged()

        }
        holder.bind(event,selectedPosition)
    }


    class ColorViewHolder(private var binding: ItemAddEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, selectedPosition: Int) {
            binding.event = event
            if(event.status!!) {
                binding.imageTag.setImageResource(R.drawable.tag_payment)
            } else {
                binding.imageTag.setImageResource(R.drawable.tag_breakfast)
            }

            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

}