package com.snc.farmaccount.statistic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.databinding.ItemStatisticTagBinding

class StatisticTagAdapter(private val budget: ArrayList<StatisticCatalog>, var onClickListener: OnClickListener)
    : RecyclerView.Adapter<StatisticTagAdapter.TagViewHolder>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder( ItemStatisticTagBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        var catalog: StatisticCatalog = budget[position]
        holder.itemView.setOnClickListener {
            selectedPosition = position
            onClickListener.onClick(catalog)
            notifyDataSetChanged()
        }
        holder.bind(catalog,selectedPosition)
    }

    override fun getItemCount() = budget.size

    class TagViewHolder(private var binding: ItemStatisticTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(catalog: StatisticCatalog, selectedPosition:Int) {
            binding.tag = catalog
            binding.statisticCatalog.text = catalog.name
            if (selectedPosition==adapterPosition) {
                binding.statisticCatalog.setTextColor(ApplicationContext.applicationContext().getColor(R.color.wood))
                binding.imageCatalog.setImageResource(R.drawable.datepicker_border)
            } else {
                binding.statisticCatalog.setTextColor(ApplicationContext.applicationContext().getColor(R.color.expend_title))
                binding.imageCatalog.setImageResource(R.drawable.tag_border)
            }
            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (catalog: StatisticCatalog ) -> Unit) {
        fun onClick(catalog: StatisticCatalog ) = clickListener(catalog)
    }
}