package com.snc.farmaccount.home

import android.annotation.SuppressLint
import android.graphics.Color
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

        @SuppressLint("ResourceAsColor")
        fun bind(event: Event, selectedPosition: Int) {
            binding.event = event
            if (event.tag == "早餐") {
                binding.imageTag.setImageResource(R.drawable.fried_egg)
            }
            if (event.tag == "午餐") {
                binding.imageTag.setImageResource(R.drawable.pig)
            }
            if (event.tag == "晚餐") {
                binding.imageTag.setImageResource(R.drawable.cow)
            }
            if (event.tag == "點心") {
                binding.imageTag.setImageResource(R.drawable.gingerbread_man)
            }
            if (event.tag == "衣服") {
                binding.imageTag.setImageResource(R.drawable.apron)
            }
            if (event.tag == "生活") {
                binding.imageTag.setImageResource(R.drawable.field)
            }
            if (event.tag == "交通") {
                binding.imageTag.setImageResource(R.drawable.tractor)
            }
            if (event.tag == "娛樂") {
                binding.imageTag.setImageResource(R.drawable.kite)
            }
            if (event.tag == "薪水") {
                binding.imageTag.setImageResource(R.drawable.money)
            }
            if (event.tag == "中獎") {
                binding.imageTag.setImageResource(R.drawable.ticket)
            }

            if (event.status == true) {
                binding.price.setBackgroundResource(R.drawable.radius_border_green)
                binding.titleTag.setTextColor(Color.parseColor("#82B763"))
            } else {
                binding.price.setBackgroundResource(R.drawable.radius_border_red)
                binding.titleTag.setTextColor(Color.parseColor("#BC4141"))
            }

            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

}