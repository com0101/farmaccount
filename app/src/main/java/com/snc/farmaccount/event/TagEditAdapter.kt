package com.snc.farmaccount.event

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.ItemEventTagBinding

class TagEditAdapter(private var onClickListener: OnClickListener,var viewModel: EditEventViewModel):
    ListAdapter<Tag, TagEditAdapter.EventTagViewHolder>(DiffCallback){

    var selectedPosition = -1
    var select = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventTagViewHolder {
        return EventTagViewHolder (
            ItemEventTagBinding
                .inflate(LayoutInflater.from(parent.context ), parent, false))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: EventTagViewHolder, position: Int) {
        val event : Tag = getItem(position)
        holder.itemView.setOnClickListener {
            selectedPosition = position
            select = true
            onClickListener.onClick(event)
            notifyDataSetChanged()
        }
        holder.bind(event,selectedPosition,viewModel,select)
    }

    class EventTagViewHolder(private var binding: ItemEventTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event : Tag, selectedPosition: Int,
                 viewModel: EditEventViewModel,select: Boolean) {
            binding.tag = event
            binding.textEvent.text = event.tag_name
            binding.eventTag.setImageResource(event.tag_img)
            if (selectedPosition==adapterPosition) {
                viewModel.chooseTag.value = event
                Log.i("Sophie_tag","${viewModel.chooseTag.value}")
//                binding.eventTag.setImageResource(event.tag_img_press)
                select
            } else {
                binding.eventTag.setImageResource(event.tag_img)
                !select
            }
            if (viewModel.detail.value?.tag == event.tag_name) {
                binding.eventTag.setImageResource(event.tag_img_press)
            }



            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (event : Tag) -> Unit) {
        fun onClick(event : Tag) = clickListener(event)
    }

}