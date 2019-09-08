package com.snc.farmaccount.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.SumEvent
import com.snc.farmaccount.databinding.ItemStatisticEventBinding
import com.snc.farmaccount.databinding.ItemStatisticTotalBinding

class StatisticCatagoryAdapter(private val budget: ArrayList<SumEvent>,
                               var onClickListener: OnClickListener,
                               var viewModel: StatisticViewModel)
    : RecyclerView.Adapter<StatisticCatagoryAdapter.StatisticViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        return StatisticViewHolder( ItemStatisticTotalBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        var event: SumEvent = budget[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(event)
            notifyDataSetChanged()
        }
        holder.bind(event, viewModel)
    }

    override fun getItemCount() = budget.size

    class StatisticViewHolder(private var binding: ItemStatisticTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: SumEvent, viewModel: StatisticViewModel) {
            binding.sum = event
            binding.viewModel = viewModel
            if (event.tagType == "食") {
                binding.imageTag.setImageResource(R.drawable.tag_eat)
            }
            if (event.tagType == "衣") {
                binding.imageTag.setImageResource(R.drawable.tag_cloth)
            }
            if (event.tagType == "住") {
                binding.imageTag.setImageResource(R.drawable.tag_live)
            }
            if (event.tagType == "行") {
                binding.imageTag.setImageResource(R.drawable.tag_traffic)
            }
            if (event.tagType == "樂") {
                binding.imageTag.setImageResource(R.drawable.tag_fun)
            }
            if (event.tagType == "收入") {
                binding.imageTag.setImageResource(R.drawable.tag_income)
            }

            binding.titleTag.text = event.tagType
            binding.textPrice.text = viewModel.eventByTotalPrice.value.toString()

            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (event: SumEvent) -> Unit) {
        fun onClick(event: SumEvent) = clickListener(event)
    }
}