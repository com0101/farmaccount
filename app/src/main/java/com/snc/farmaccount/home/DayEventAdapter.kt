package com.snc.farmaccount.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.databinding.ItemAddEventBinding

class DayEventAdapter(var onClickListener: OnClickListener,var viewModel: DayViewModel):
    ListAdapter<Event, DayEventAdapter.ColorViewHolder>(DiffCallback){

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
            onClickListener.onClick(event)
            notifyDataSetChanged()

        }
        holder.bind(event, viewModel)
    }

    class ColorViewHolder(private var binding: ItemAddEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(event: Event, viewModel: DayViewModel) {
            binding.event = event
            binding.viewModel = viewModel

            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_breakfast)) {
                binding.imageTag.setImageResource(R.drawable.fried_egg)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_lunch)) {
                binding.imageTag.setImageResource(R.drawable.pig)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_dinner)) {
                binding.imageTag.setImageResource(R.drawable.cow)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_dessert)) {
                binding.imageTag.setImageResource(R.drawable.gingerbread_man)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_cloth)) {
                binding.imageTag.setImageResource(R.drawable.apron)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_live)) {
                binding.imageTag.setImageResource(R.drawable.field)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_traffic)) {
                binding.imageTag.setImageResource(R.drawable.tractor)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_fun)) {
                binding.imageTag.setImageResource(R.drawable.kite)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_payment)) {
                binding.imageTag.setImageResource(R.drawable.money)
            }
            if (event.tag == ApplicationContext.applicationContext().getString(R.string.tag_lottery)) {
                binding.imageTag.setImageResource(R.drawable.ticket)
            }

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

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

}