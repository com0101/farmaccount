package com.snc.farmaccount.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.databinding.ItemStatisticEventBinding

class StatisticCatalogAdapter(private val budget: ArrayList<Event>, var onClickListener: OnClickListener)
    : RecyclerView.Adapter<StatisticCatalogAdapter.CatalogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder( ItemStatisticEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        var event: Event = budget[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(event)
            notifyDataSetChanged()
        }
        holder.bind(event)
    }

    override fun getItemCount() = budget.size

    class CatalogViewHolder(private var binding: ItemStatisticEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.event = event
            if (event.catalog == "食") {
                binding.imageTag.setImageResource(R.drawable.tag_eat)
            }
            if (event.catalog == "衣") {
                binding.imageTag.setImageResource(R.drawable.tag_cloth)
            }
            if (event.catalog == "住") {
                binding.imageTag.setImageResource(R.drawable.tag_live)
            }
            if (event.catalog == "行") {
                binding.imageTag.setImageResource(R.drawable.tag_traffic)
            }
            if (event.catalog == "樂") {
                binding.imageTag.setImageResource(R.drawable.tag_fun)
            }
            if (event.catalog == "收入") {
                binding.imageTag.setImageResource(R.drawable.tag_income)
            }

            binding.titleTag.text = event.catalog

            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }
}