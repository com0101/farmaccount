package com.snc.farmaccount.choose

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.databinding.ItemFarmTypeBinding

class ChooseAdapter(private val budget: ArrayList<Budget>, var onClickListener: OnClickListener) : RecyclerView.Adapter<ChooseAdapter.BudgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder( ItemFarmTypeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        var budget: Budget = budget[position]
        holder.itemView.setOnClickListener {

            onClickListener.onClick(budget)
            notifyDataSetChanged()

        }
        holder.bind(budget)
    }

    override fun getItemCount() = budget.size

    class BudgetViewHolder(private var binding: ItemFarmTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: Budget) {
            binding.budget = budget
            binding.budgetFarmImage.setImageResource(budget.farmImage)
            binding.type.setImageResource(budget.farmtype)


            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (budget: Budget) -> Unit) {
        fun onClick(budget: Budget) = clickListener(budget)
    }
}