package com.snc.farmaccount.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.databinding.ItemFarmEditBinding
import com.snc.farmaccount.databinding.ItemStatisticTagBinding

class StatisticTagAdapter(private val budget: ArrayList<StatisticCatalog>, var onClickListener: OnClickListener)
    : RecyclerView.Adapter<StatisticTagAdapter.TagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder( ItemStatisticTagBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        var catalog: StatisticCatalog = budget[position]
        holder.itemView.setOnClickListener {

            onClickListener.onClick(catalog)
            notifyDataSetChanged()

        }
        holder.bind(catalog)
    }

    override fun getItemCount() = budget.size

    class TagViewHolder(private var binding: ItemStatisticTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(catalog: StatisticCatalog ) {
            binding.tag = catalog
            binding.statisticCatalog.text = catalog.name
            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (catalog: StatisticCatalog ) -> Unit) {
        fun onClick(catalog: StatisticCatalog ) = clickListener(catalog)
    }
}